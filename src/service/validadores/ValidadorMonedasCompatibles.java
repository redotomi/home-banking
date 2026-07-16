package service.validadores;

import entidades.Cuenta;
import exceptions.serviceExceptions.MonedasIncompatiblesException;
import exceptions.serviceExceptions.ServiceException;

public class ValidadorMonedasCompatibles implements ValidadorTransferencia {
    
    @Override
    public void validar(Cuenta origen, Cuenta destino, int monto) throws ServiceException {
        if (!origen.getMoneda().equals(destino.getMoneda())) {
            throw new MonedasIncompatiblesException(origen.getMoneda(), destino.getMoneda());
        }
    }
}
