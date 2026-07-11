package exceptions.serviceExceptions;

public class CuentaNoEncontradaException extends ServiceException {

    public CuentaNoEncontradaException(String cbu) {
        super("No existe una cuenta con CBU: " + cbu);
    }
}
