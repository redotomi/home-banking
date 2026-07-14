package service;

import dao.MovimientoTarjetaDAO;
import entidades.MovimientoTarjeta;
import entidades.Tarjeta;
import exceptions.DAOExceptions.DAOException;
import exceptions.serviceExceptions.ServiceException;

import java.util.List;

public class MovimientoTarjetaService {

    private final MovimientoTarjetaDAO movimientoDAO;
    private final TarjetaService       tarjetaService;

    public MovimientoTarjetaService(MovimientoTarjetaDAO movimientoDAO, TarjetaService tarjetaService) {
        this.movimientoDAO  = movimientoDAO;
        this.tarjetaService = tarjetaService;
    }

    /**
     * Registra un débito en la tarjeta. Valida que el nuevo consumo no supere
     * el límite antes de persistir el movimiento y actualizar el campo consumo.
     */
    public void registrarMovimiento(String numeroTarjeta, int monto, String referencia) throws ServiceException {
        if (monto <= 0) {
            throw new ServiceException("El monto del movimiento debe ser mayor a cero.");
        }
        if (referencia == null || referencia.trim().isEmpty()) {
            throw new ServiceException("La referencia no puede estar vacía.");
        }

        // Validar límite disponible
        Tarjeta tarjeta = tarjetaService.buscarTarjeta(numeroTarjeta);
        int nuevoConsumo = tarjeta.getConsumo() + monto;
        if (nuevoConsumo > tarjeta.getLimite()) {
            int disponible = tarjeta.getLimite() - tarjeta.getConsumo();
            throw new ServiceException(
                "El monto supera el límite disponible. Disponible: $" + disponible
                + " | Límite: $" + tarjeta.getLimite()
            );
        }

        // Persistir el movimiento (fecha la pone la BD por DEFAULT)
        MovimientoTarjeta movimiento = new MovimientoTarjeta(numeroTarjeta, monto, null, referencia.trim());
        try {
            movimientoDAO.registrarMovimiento(movimiento);
        } catch (DAOException e) {
            throw new ServiceException("Error al registrar el movimiento.", e);
        }

        // Actualizar el consumo en la tarjeta
        tarjetaService.actualizarConsumo(numeroTarjeta, nuevoConsumo);
    }

    public List<MovimientoTarjeta> listarMovimientosPorTarjeta(String numeroTarjeta) throws ServiceException {
        try {
            return movimientoDAO.listarMovimientosPorTarjeta(numeroTarjeta);
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener los movimientos de la tarjeta: " + numeroTarjeta, e);
        }
    }
}
