package exceptions.serviceExceptions;

public class UsuarioNoEncontradoException extends ServiceException {

    public UsuarioNoEncontradoException(int dni) {
        super("No existe un usuario con el DNI: " + dni);
    }

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}
