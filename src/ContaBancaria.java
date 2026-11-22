import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaBancaria {

    private static final BigDecimal LIMITE_INICIAL_ATE_500 = new BigDecimal("50.00");
    private static final BigDecimal PERCENTUAL_CHEQUE_ESPECIAL = new BigDecimal("0.50");
    private static final BigDecimal TAXA_CHEQUE_ESPECIAL = new BigDecimal("0.20");

    private final String numeroConta;
    private final Cliente titular;
    private BigDecimal saldo;
    private BigDecimal limiteChequeEspecial;
    private BigDecimal chequeEspecialUsado;

    public ContaBancaria(String numeroConta, Cliente titular, BigDecimal depositoInicial) {
        if (depositoInicial == null || depositoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O depósito inicial não pode ser negativo.");
        }

        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = depositoInicial;
        this.chequeEspecialUsado = BigDecimal.ZERO;

        if (depositoInicial.compareTo(new BigDecimal("500.00")) <= 0) {
            this.limiteChequeEspecial = LIMITE_INICIAL_ATE_500;
        } else {
            this.limiteChequeEspecial = depositoInicial
                    .multiply(PERCENTUAL_CHEQUE_ESPECIAL)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal consultarSaldo() {
        return this.saldo;
    }

    public BigDecimal consultarChequeEspecial() {
        return this.limiteChequeEspecial;
    }

    public BigDecimal consultarChequeEspecialDisponivel() {
        return this.limiteChequeEspecial.subtract(this.chequeEspecialUsado);
    }

    public boolean estaUsandoChequeEspecial() {
        return this.chequeEspecialUsado.compareTo(BigDecimal.ZERO) > 0;
    }

    public void depositar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Valor de depósito inválido.");
            return;
        }

        BigDecimal restante = valor;

        if (estaUsandoChequeEspecial()) {
            BigDecimal taxaSobreUsoAtual = this.chequeEspecialUsado
                    .multiply(TAXA_CHEQUE_ESPECIAL)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalDividaCheque = this.chequeEspecialUsado.add(taxaSobreUsoAtual);

            if (restante.compareTo(totalDividaCheque) >= 0) {
                restante = restante.subtract(totalDividaCheque);
                this.chequeEspecialUsado = BigDecimal.ZERO;
            } else {
                // Abate proporcionalmente o uso do cheque especial e a taxa de 20%
                BigDecimal fatorTotal = BigDecimal.ONE.add(TAXA_CHEQUE_ESPECIAL);
                BigDecimal amortizacaoUso = restante.divide(fatorTotal, 2, RoundingMode.DOWN);
                if (amortizacaoUso.compareTo(this.chequeEspecialUsado) > 0) {
                    amortizacaoUso = this.chequeEspecialUsado;
                }
                this.chequeEspecialUsado = this.chequeEspecialUsado.subtract(amortizacaoUso);
                restante = BigDecimal.ZERO;
            }
        }

        if (restante.compareTo(BigDecimal.ZERO) > 0) {
            this.saldo = this.saldo.add(restante);
        }
    }

    public boolean sacar(BigDecimal valor) {
        return movimentarSaida(valor, "Saque");
    }

    public boolean pagarBoleto(BigDecimal valor) {
        return movimentarSaida(valor, "Pagamento de boleto");
    }

    private boolean movimentarSaida(BigDecimal valor, String operacao) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println(operacao + ": valor inválido.");
            return false;
        }

        BigDecimal disponivelTotal = this.saldo.add(consultarChequeEspecialDisponivel());
        if (valor.compareTo(disponivelTotal) > 0) {
            System.out.println(operacao + " negado: saldo e cheque especial insuficientes.");
            return false;
        }

        if (this.saldo.compareTo(valor) >= 0) {
            this.saldo = this.saldo.subtract(valor);
        } else {
            BigDecimal diferencaCheque = valor.subtract(this.saldo);
            this.saldo = BigDecimal.ZERO;
            this.chequeEspecialUsado = this.chequeEspecialUsado.add(diferencaCheque);
        }

        return true;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public Cliente getTitular() {
        return titular;
    }

    public BigDecimal getChequeEspecialUsado() {
        return chequeEspecialUsado;
    }
}