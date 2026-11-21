# Semana 10 — TDD

Quatro projetos Maven independentes, baseados nos materiais da disciplina. Requer Java 17 ou superior e Maven 3. Execute, em cada pasta:

```bash
mvn clean verify
```

O JaCoCo gera `target/site/jacoco/index.html`; a etapa `verify` exige 100% de linhas e ramificações cobertas. Os diretórios `target` são gerados e ignorados pelo Git.

| Projeto | Comportamento | Testes | Linhas | Ramificações |
|---|---|---:|---:|---:|
| bubblesort-tdd | Ordenação crescente, inclusive vazios e repetidos | 5 | 100% | 100% |
| carrinho-compras-tdd | Adição, remoção, preço válido e desconto a partir de R$ 500 | 10 | 100% | 100% |
| conta-bancaria-tdd-maven | Depósitos, saques e proteção contra operações inválidas | 8 | 100% | 100% |
| estoque-tdd | Entradas e saídas sem estoque negativo | 10 | 100% | 100% |

Os testes fornecidos para Bubble Sort, conta e estoque foram preservados; casos adicionais foram colocados em classes separadas. Para o carrinho, foram escritos testes a partir dos requisitos. A execução inicial demonstrou falhas nos projetos incompletos; no carrinho, a ausência dos métodos causou falha de compilação. Em seguida foram implementados os comportamentos e executada a verificação final. Essas execuções registram uma fase vermelha e uma verde por projeto; não constituem registro de um ciclo separado para cada requisito.

Decisões: no carrinho, preços negativos ou não finitos lançam `IllegalArgumentException`; produto nulo é rejeitado. A remoção usa a identidade do objeto e remove uma ocorrência. Na conta, valores não positivos ou não finitos e saques superiores ao saldo são ignorados. No estoque, entradas e retiradas não positivas ou retiradas acima do disponível são ignoradas. As APIs monetárias seguem o `double` do material original, sem arredondamento automático; em aplicações financeiras reais, deve-se definir uma política de centavos e usar representação decimal adequada.
