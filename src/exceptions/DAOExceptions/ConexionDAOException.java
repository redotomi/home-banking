package exceptions.DAOExceptions;

public class ConexionDAOException extends DAOException {

    public ConexionDAOException(String message) {
        super(message);
    }

    public ConexionDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
