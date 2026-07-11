package exceptions.serviceExceptions;

public class MonedasIncompatiblesException extends ServiceException {

    public MonedasIncompatiblesException(String monedaOrigen, String monedaDestino) {
        super("Las monedas de las cuentas no coinciden: origen '" + monedaOrigen + "', destino '" + monedaDestino + "'");
    }
}
