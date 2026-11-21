# Conta Bancária — TDD

A conta começa com saldo zero, acumula depósitos e permite saques até o saldo disponível. Operações com valores não positivos ou não finitos e saques acima do saldo são ignorados. Os testes originais foram preservados e complementados com casos de limite.

Execute `mvn clean verify`. O relatório de cobertura fica em `target/site/jacoco/index.html`.
