package service.validadores;

import entidades.Cuenta;
import exceptions.serviceExceptions.ServiceException;

public interface ValidadorTransferencia {
    void validar(Cuenta origen, Cuenta destino, int monto) throws ServiceException;
}
