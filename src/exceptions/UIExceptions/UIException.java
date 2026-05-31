package exceptions.UIExceptions;

public class UIException extends Exception {

    public UIException(String message) {
        super(message);
    }

    public UIException(String message, Throwable cause) {
        super(message, cause);
    }

    public UIException(Throwable cause) {
        super(cause);
    }
}
