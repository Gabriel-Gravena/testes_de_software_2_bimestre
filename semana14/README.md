# Semana 14 — Teste de usuário final (Mercado Livre)

**Atividade:** executar o teste de usuário proposto em [`TESTE USUARIO FINAL.pdf`](https://github.com/JoaoChoma/teste_software_2026/blob/main/aulas/SEMANA14/TESTE%20USUARIO%20FINAL.pdf): buscar, filtrar, abrir os detalhes, adicionar ao carrinho e simular o início da compra de um fone de ouvido Bluetooth, sem concluir o pagamento.

**Entrega:** [`TESTE DE USUARIO FINAL.pdf`](TESTE%20DE%20USUARIO%20FINAL.pdf), gerado a partir do modelo preenchido (planilha de observação, resultados, critérios e problemas de usabilidade).

## Resultado em resumo

| Métrica | Resultado |
|---|---|
| Tempo de execução | 94,8 s (1 min 35 s) para as 7 tarefas |
| Taxa de sucesso | Fluxo concluído até o carrinho e o início da compra (1 de 1). 6 tarefas concluídas e 1 parcial |
| Erros | 2 (ordenação por avaliação inexistente; clique no logo com o cabeçalho fora da tela) |

| Critério de sucesso | Meta | Obtido |
|---|---|---|
| Usuários que adicionam o produto ao carrinho | ≥ 90% | 100% (1 de 1) |
| Tempo médio | até 4 min | 1 min 35 s |

## Principais achados

1. **Não existe ordenação por "melhores avaliados".** O menu só oferece Mais relevantes, Menor preço e Maior preço, então a tarefa 3 ficou parcial (o filtro Full foi aplicado).
2. O carrinho abriu com um **carregador 25 W que não foi escolhido** pelo usuário.
3. A etapa de compra começa por uma oferta de **seguro com o plano de 2 anos pré-selecionado**, sem cabeçalho visível nem caminho de volta além de "Agora não" e "Adicionar".

## Limitações

- Execução única (n = 1), com avaliador (Guilherme Bandeira) e sem acompanhamento do instrutor, então a taxa de sucesso não representa uma população.
- O formulário SUS **não foi aplicado**, pois depende de respostas de usuários reais e nenhuma nota foi inventada.
- Uma primeira tentativa, em um navegador sem sessão, foi barrada por uma tela de login e descartada. O bloqueio não foi contornado.
- Nenhum pagamento foi iniciado. Os itens colocados no carrinho foram removidos ao final.
