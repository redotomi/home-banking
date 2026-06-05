package exceptions.DAOExceptions;

public class ObjetoNoEncontradoException extends DAOException {

    public ObjetoNoEncontradoException(String message) {
        super(message);
    }

    public ObjetoNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
