# Carrinho de Compras — TDD

Adiciona e remove objetos `Produto`, soma preços e aplica desconto de 10% ao subtotal igual ou superior a R$ 500. Preços negativos e não finitos são rejeitados com `IllegalArgumentException`; produto nulo também é rejeitado. A remoção considera a própria instância e remove uma ocorrência. Os valores usam `double`, conforme o material, sem arredondamento automático.

Execute `mvn clean verify`. O relatório de cobertura fica em `target/site/jacoco/index.html`. Consulte `REQUISITOS.md` para o enunciado.
