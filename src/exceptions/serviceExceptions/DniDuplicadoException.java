package exceptions.serviceExceptions;

public class DniDuplicadoException extends ServiceException {

    public DniDuplicadoException(int dni) {
        super("Ya existe un usuario con el DNI: " + dni);
    }
}
