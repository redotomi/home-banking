package dao;

import entidades.MovimientoTarjeta;
import exceptions.DAOExceptions.DAOException;

import java.util.List;

public interface MovimientoTarjetaDAO {
    void registrarMovimiento(MovimientoTarjeta movimiento) throws DAOException;

    void borrarMovimiento(long id) throws DAOException;

    MovimientoTarjeta buscarMovimiento(long id) throws DAOException;

    List<MovimientoTarjeta> listarMovimientosPorTarjeta(String numeroTarjeta) throws DAOException;

    List<MovimientoTarjeta> listarTodosLosMovimientos() throws DAOException;
}
