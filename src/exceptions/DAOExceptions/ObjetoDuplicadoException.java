package exceptions.DAOExceptions;

public class ObjetoDuplicadoException extends DAOException {

    public ObjetoDuplicadoException(String message) {
        super(message);
    }

    public ObjetoDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
