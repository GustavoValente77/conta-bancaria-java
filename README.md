# Conta Bancária em Java

Aplicação em Java modelando operações básicas de uma conta bancária com controle de limite de cheque especial e cobrança de taxa sobre o valor utilizado.

## Funcionalidades

- Consultar saldo
- Consultar limite de cheque especial
- Depositar dinheiro
- Sacar dinheiro
- Pagar boleto
- Verificar se a conta está utilizando o cheque especial

## Regras de Negócio

- O limite do cheque especial é definido no momento da criação da conta com base no depósito inicial:
  - Depósito inicial de até **R$ 500,00**: cheque especial fixo de **R$ 50,00**.
  - Depósito inicial acima de **R$ 500,00**: cheque especial de **50%** do valor depositado.
- Saques e pagamentos de boleto consideram o saldo disponível somado ao limite restante do cheque especial.
- Quando há uso do cheque especial, os próximos depósitos priorizam a quitação do valor utilizado e a aplicação da taxa de **20%** sobre o uso do cheque especial antes de compor saldo livre.

## Decisões Técnicas

- Uso de `BigDecimal` para precisão em cálculos monetários, evitando problemas de ponto flutuante.
- Separação entre dados do titular (`Cliente`) e regras financeiras da conta (`ContaBancaria`).

## Como Executar

1. Abra o projeto no **IntelliJ IDEA** (ou compile os arquivos `.java` via terminal com `javac`).
2. Execute a classe `Main` para testar os cenários de depósito, saque, pagamento de boleto e uso do cheque especial.
