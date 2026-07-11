package service;

import dao.TransferenciaDAO;
import entidades.Cuenta;
import entidades.Transferencia;
import exceptions.DAOExceptions.DAOException;
import exceptions.serviceExceptions.CuentaNoEncontradaException;
import exceptions.serviceExceptions.MonedasIncompatiblesException;
import exceptions.serviceExceptions.SaldoInsuficienteException;
import exceptions.serviceExceptions.ServiceException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class TransferenciaService {

    private final TransferenciaDAO transferenciaDAO;
    private final CuentaService cuentaService;

    public TransferenciaService(TransferenciaDAO transferenciaDAO, CuentaService cuentaService) {
        this.transferenciaDAO = transferenciaDAO;
        this.cuentaService = cuentaService;
    }

    public void realizarTransferencia(String cbuOrigen, String cbuDestino, int monto) throws ServiceException {
        Cuenta origen  = cuentaService.muestraCuenta(cbuOrigen);
        Cuenta destino = cuentaService.muestraCuenta(cbuDestino);

        validarMonedas(origen, destino);
        validarSaldo(origen, monto);

        int nuevoSaldoOrigen  = origen.getMonto() - monto;
        int nuevoSaldoDestino = destino.getMonto() + monto;

        cuentaService.actualizarSaldo(cbuOrigen, nuevoSaldoOrigen);

        try {
            cuentaService.actualizarSaldo(cbuDestino, nuevoSaldoDestino);
        } catch (ServiceException e) {
            cuentaService.actualizarSaldo(cbuOrigen, origen.getMonto());
            throw e;
        }

        Transferencia transferencia = new Transferencia(
                cbuOrigen,
                cbuDestino,
                origen.getMoneda(),
                monto,
                Timestamp.from(Instant.now())
        );

        try {
            transferenciaDAO.crearTransferencia(transferencia);
        } catch (DAOException e) {
            cuentaService.actualizarSaldo(cbuOrigen, origen.getMonto());
            cuentaService.actualizarSaldo(cbuDestino, destino.getMonto());
            throw new ServiceException("Error al registrar la transferencia. Se revirtieron los saldos.", e);
        }
    }

    public List<Transferencia> listarTransferenciasDeUsuario(int dniUsuario) throws ServiceException {
        try {
            return transferenciaDAO.listaTransferenciasUsuario(dniUsuario);
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener las transferencias del usuario con DNI: " + dniUsuario, e);
        }
    }

    private void validarMonedas(Cuenta origen, Cuenta destino) throws MonedasIncompatiblesException {
        if (!origen.getMoneda().equals(destino.getMoneda())) {
            throw new MonedasIncompatiblesException(origen.getMoneda(), destino.getMoneda());
        }
    }

    private void validarSaldo(Cuenta origen, int monto) throws SaldoInsuficienteException {
        if (origen.getMonto() < monto) {
            throw new SaldoInsuficienteException(origen.getMonto(), monto);
        }
    }
}
