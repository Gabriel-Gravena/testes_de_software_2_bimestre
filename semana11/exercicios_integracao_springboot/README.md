# Semana 11 — Testes de integração com Spring Boot e JUnit 5

**Tecnologias:** Java 17, Maven, Spring Boot 3.3, Spring Data JPA, H2 (em memória), JUnit 5 e MockMvc.

## O exercício

O projeto-base traz uma API de produtos já funcional (`Controller → Service → Repository → H2`) e duas classes de teste com métodos `TODO`. A atividade pede completar esses testes **sem usar mocks** de `ProdutoRepository` ou `ProdutoService`, de modo que cada teste passe pelas camadas reais da aplicação e pelo banco H2.

```
src/main/java/br/edu/exemplo
├── Aplicacao.java                     # classe @SpringBootApplication
├── controller/ProdutoController.java  # POST /produtos e GET /produtos/{id}
├── controller/ProdutoRequest.java     # record com nome e preço (corpo do POST)
├── model/Produto.java                 # entidade JPA
├── repository/ProdutoRepository.java  # JpaRepository<Produto, Long>
└── service/ProdutoService.java        # regras: nome obrigatório e preço > 0

src/test/java/br/edu/exemplo
├── ProdutoServiceIntegrationTest.java     # Exercício 1 (CT01, CT02, CT03)
└── ProdutoControllerIntegrationTest.java  # Exercício 2 (CT04, CT05)
```

## Como executar

O projeto inclui o Maven Wrapper, então não é preciso ter o Maven instalado (só um JDK 17 ou mais recente):

```bash
# Linux / macOS / Git Bash
./mvnw test

# Windows (PowerShell ou CMD)
.\mvnw.cmd test
```

## Exercício 1 — Service + Repository + H2

Classe: [`ProdutoServiceIntegrationTest`](src/test/java/br/edu/exemplo/ProdutoServiceIntegrationTest.java), anotada com `@SpringBootTest` (sobe o contexto Spring completo) e `@Transactional` (cada teste roda numa transação desfeita ao final).

| Caso | Método | O que o teste faz | Verificação |
|---|---|---|---|
| CT01 — Cadastro válido | `deveCadastrarERecuperarProduto` | `service.cadastrar("Teclado", 150.0)` e depois `repository.findById(id)` | `assertNotNull` no ID; `assertEquals` no nome `Teclado` e no preço `150.0` do registro lido do repositório |
| CT02 — Nome inválido | `naoDevePersistirProdutoComNomeEmBranco` | Guarda `repository.count()`, tenta `service.cadastrar("   ", 80.0)` | `assertThrows(IllegalArgumentException.class)` com a mensagem `Nome obrigatório`; `count()` continua igual |
| CT03 — Atualização (desafio) | `deveAtualizarPrecoPersistido` | Cadastra `Mouse` por `80.0`, chama `service.atualizarPreco(id, 95.0)`, faz `flush()` e `clear()` no `EntityManager` e consulta o repositório | Preço lido do banco é `95.0` |

No CT03, o `flush()` grava a alteração no H2 e o `clear()` esvazia o cache de primeiro nível do Hibernate. Assim o `findById` seguinte faz um `SELECT` de verdade em vez de devolver o mesmo objeto que já estava em memória, e o teste confirma que o novo preço foi **persistido**, não só alterado no objeto.

### Discussão

**Por que consultar com `repository.findById()` é uma evidência diferente de olhar só o objeto retornado pelo serviço?**
O objeto retornado por `service.cadastrar(...)` prova apenas que o método devolveu algo com os valores esperados. Ele poderia ter sido montado em memória sem nunca chegar ao banco. Ao buscar pelo repositório usando o ID gerado, o teste verifica que o registro existe na camada de persistência, que o ID foi atribuído pelo banco (`IDENTITY`) e que o mapeamento JPA dos campos `nome` e `preco` está correto. É isso que caracteriza o teste como de *integração*: a evidência vem de outro componente (repositório/H2), e não do próprio objeto testado.

**O que `@Transactional` faz ao término de cada teste?**
Em classes de teste, o Spring abre uma transação antes de cada método e faz **rollback** automaticamente no final, em vez de commit. Tudo o que o teste inseriu ou alterou no H2 é desfeito, então cada teste começa com o banco no mesmo estado e não depende da ordem de execução. Por isso o CT02 pode comparar `count()` antes e depois sem ser afetado pelo produto inserido no CT01.

## Exercício 2 — HTTP com MockMvc

Classe: [`ProdutoControllerIntegrationTest`](src/test/java/br/edu/exemplo/ProdutoControllerIntegrationTest.java), anotada com `@SpringBootTest`, `@AutoConfigureMockMvc` e `@Transactional`.

| Caso | Método | Requisição | Verificação |
|---|---|---|---|
| CT04 — Cadastro via API | `deveCadastrarProdutoPelaApi` | `POST /produtos` com `{"nome":"Mouse","preco":80.0}` e `Content-Type: application/json` | `status().isOk()` (200); resposta JSON; `jsonPath("$.id").isNumber()`, `$.nome = "Mouse"`, `$.preco = 80.0` |
| CT05 — Consulta via API | `deveBuscarProdutoPelaApi` | Faz o POST, lê o corpo com `ObjectMapper.readTree(...)` para extrair o `id`, depois `GET /produtos/{id}` | `status().isOk()` (200); `$.id` igual ao ID extraído, `$.nome = "Mouse"`, `$.preco = 80.0` |

### Discussão

**Quais componentes são efetivamente exercitados?**
Todo o caminho da requisição dentro da aplicação: o `DispatcherServlet` do Spring MVC, o roteamento para `ProdutoController`, a desserialização do JSON para `ProdutoRequest` e a serialização de `Produto` para JSON (Jackson), as regras de `ProdutoService`, o `ProdutoRepository` (Spring Data JPA / Hibernate) e o banco H2. No CT05, o GET depende do registro que o POST gravou no banco, o que confirma que as camadas estão conectadas de ponta a ponta.

**Por que o MockMvc não precisa iniciar um servidor HTTP real?**
O MockMvc cria objetos de requisição e resposta simulados (`MockHttpServletRequest` e `MockHttpServletResponse`) e os entrega diretamente ao `DispatcherServlet`, dentro da mesma JVM do teste. Não há Tomcat escutando em uma porta nem tráfego de rede, mas todo o processamento do Spring MVC (filtros, conversores de mensagem, controllers, tratamento de status) acontece normalmente. O teste fica mais rápido e determinístico, sem deixar de cobrir a camada web.

**Qual a diferença entre esse teste e um teste unitário do controller com dependências simuladas?**
Num teste unitário (por exemplo com `@WebMvcTest` e `@MockBean ProdutoService`), o service é um dublê: o teste só verifica se o controller chama o método certo e repassa o retorno, e o comportamento do service e do banco é inventado pelo próprio teste. Aqui nada é simulado além do transporte HTTP. As regras reais de validação, o mapeamento JPA, a geração de ID pelo banco e a serialização são exercitados juntos, o que permite detectar falhas de *integração* entre as camadas, como um campo que não é persistido ou um JSON com nome de propriedade errado. Em troca, o teste é mais lento e, quando falha, aponta o problema com menos precisão.

## Isolamento dos testes

- **Banco em memória:** `application.properties` usa `jdbc:h2:mem:produtos` com `ddl-auto=create-drop`. O esquema é criado ao subir o contexto e descartado no final, sem depender de nenhum banco externo.
- **Rollback por teste:** `@Transactional` nas duas classes desfaz as alterações de cada método, inclusive as feitas pelas requisições do MockMvc, que rodam na mesma thread e, por isso, na mesma transação do teste.
- **Sem mocks:** nenhuma classe de produção é substituída. O único elemento simulado é a camada de transporte HTTP (MockMvc).
- **Independência de ordem:** como o banco volta ao estado inicial depois de cada teste, eles podem rodar em qualquer ordem. Os testes nunca supõem um ID fixo: sempre usam o ID devolvido pela aplicação.

## Evidência de execução

Saída do `./mvnw test`:

```
[INFO] Running br.edu.exemplo.ProdutoControllerIntegrationTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.417 s -- in br.edu.exemplo.ProdutoControllerIntegrationTest
[INFO] Running br.edu.exemplo.ProdutoServiceIntegrationTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.006 s -- in br.edu.exemplo.ProdutoServiceIntegrationTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

| Caso | Resultado |
|---|---|
| CT01 — Cadastro válido | ✅ Passou |
| CT02 — Nome inválido | ✅ Passou |
| CT03 — Atualização (desafio) | ✅ Passou |
| CT04 — Cadastro via API | ✅ Passou |
| CT05 — Consulta via API | ✅ Passou |
