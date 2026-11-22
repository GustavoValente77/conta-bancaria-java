import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Cliente cliente = new Cliente("Ana Souza", "123.456.789-00");
        ContaBancaria conta = new ContaBancaria("0001-9", cliente, new BigDecimal("600.00"));

        System.out.println("Titular: " + conta.getTitular().getNome());
        System.out.println("Saldo inicial: " + conta.consultarSaldo());
        System.out.println("Cheque especial: " + conta.consultarChequeEspecial());

        conta.sacar(new BigDecimal("650.00"));
        System.out.println("Usando cheque especial? " + conta.estaUsandoChequeEspecial());
        System.out.println("Valor usado do cheque: " + conta.getChequeEspecialUsado());

        conta.depositar(new BigDecimal("100.00"));
        System.out.println("Após depósito de 100 - Saldo: " + conta.consultarSaldo());
        System.out.println("Após depósito de 100 - Usando cheque? " + conta.estaUsandoChequeEspecial());
    }
}