package service.validadores;

import entidades.Cuenta;
import exceptions.serviceExceptions.SaldoInsuficienteException;
import exceptions.serviceExceptions.ServiceException;

public class ValidadorSaldoSuficiente implements ValidadorTransferencia {

    @Override
    public void validar(Cuenta origen, Cuenta destino, int monto) throws ServiceException {
        if (origen.getMonto() < monto) {
            throw new SaldoInsuficienteException(origen.getMonto(), monto);
        }
    }
}
