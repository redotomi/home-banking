package dao.impl;

import dao.MovimientoTarjetaDAO;
import entidades.MovimientoTarjeta;
import exceptions.DAOExceptions.ConexionDAOException;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoNoEncontradoException;
import util.DBManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoTarjetaDAOImplH2 extends AbstractDAO implements MovimientoTarjetaDAO {

    public void registrarMovimiento(MovimientoTarjeta movimiento) throws DAOException {
        String sql = "INSERT INTO MOVIMIENTOS (numeroTarjeta, monto, referencia) "
                   + "VALUES ('" + movimiento.getNumeroTarjeta() + "', "
                   + movimiento.getMonto() + ", '"
                   + movimiento.getReferencia() + "')";

        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = s.getGeneratedKeys();
            if (keys.next()) {
                movimiento.setId(keys.getLong(1));
            }
            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al registrar el movimiento de la tarjeta: " + movimiento.getNumeroTarjeta(), e);
        } finally {
            cerrarConexion(c);
        }
    }

    public void borrarMovimiento(long id) throws DAOException {
        String sql = "DELETE FROM MOVIMIENTOS WHERE id = " + id;
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            int filasBorradas = s.executeUpdate(sql);
            if (filasBorradas == 0) {
                throw new ObjetoNoEncontradoException("No existe un movimiento con id: " + id);
            }
            c.commit();
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al eliminar el movimiento con id: " + id, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public MovimientoTarjeta buscarMovimiento(long id) throws DAOException {
        String sql = "SELECT * FROM MOVIMIENTOS WHERE id = " + id;
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                return mapearMovimiento(rs);
            }
            throw new ObjetoNoEncontradoException("No existe un movimiento con id: " + id);
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al buscar el movimiento con id: " + id, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public List<MovimientoTarjeta> listarMovimientosPorTarjeta(String numeroTarjeta) throws DAOException {
        List<MovimientoTarjeta> resultado = new ArrayList<>();
        String sql = "SELECT * FROM MOVIMIENTOS WHERE numeroTarjeta = '" + numeroTarjeta + "' ORDER BY fecha DESC";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                resultado.add(mapearMovimiento(rs));
            }
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al listar los movimientos de la tarjeta: " + numeroTarjeta, e);
        } finally {
            cerrarConexion(c);
        }
        return resultado;
    }

    public List<MovimientoTarjeta> listarTodosLosMovimientos() throws DAOException {
        List<MovimientoTarjeta> resultado = new ArrayList<>();
        String sql = "SELECT * FROM MOVIMIENTOS ORDER BY fecha DESC";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                resultado.add(mapearMovimiento(rs));
            }
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al listar todos los movimientos", e);
        } finally {
            cerrarConexion(c);
        }
        return resultado;
    }

    private MovimientoTarjeta mapearMovimiento(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String numeroTarjeta = rs.getString("numeroTarjeta");
        int monto = rs.getInt("monto");
        LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
        String referencia = rs.getString("referencia");
        return new MovimientoTarjeta(id, numeroTarjeta, monto, fecha, referencia);
    }

}
