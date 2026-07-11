package exceptions.serviceExceptions;

public class SaldoInsuficienteException extends ServiceException {

    public SaldoInsuficienteException(int saldoActual, int montoSolicitado) {
        super("Saldo insuficiente. Saldo disponible: " + saldoActual + ", monto solicitado: " + montoSolicitado);
    }
}
