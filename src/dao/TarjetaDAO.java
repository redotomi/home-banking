package dao;

import entidades.Tarjeta;
import exceptions.DAOExceptions.DAOException;

import java.util.List;

public interface TarjetaDAO {
    void crearTarjeta(Tarjeta tarjeta) throws DAOException;

    void borrarTarjeta(String numero) throws DAOException;

    void actualizarTarjeta(Tarjeta tarjeta) throws DAOException;

    Tarjeta buscarTarjeta(String numero) throws DAOException;

    List<Tarjeta> listarTarjetasPorUsuario(String dniUsuario) throws DAOException;

    List<Tarjeta> listarTodasLasTarjetas() throws DAOException;
}
