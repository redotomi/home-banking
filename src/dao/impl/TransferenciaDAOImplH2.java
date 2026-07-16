package dao.impl;

import dao.TransferenciaDAO;
import entidades.Transferencia;
import exceptions.DAOExceptions.ConexionDAOException;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoNoEncontradoException;
import util.DBManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaDAOImplH2 extends AbstractDAO implements TransferenciaDAO {

    public void crearTransferencia(Transferencia unaTransferencia) throws DAOException {
        String cbuOrigen = unaTransferencia.getCbuCuentaOrigen();
        String cbuDestino = unaTransferencia.getCbuCuentaDestino();
        String moneda = unaTransferencia.getMoneda();
        int monto = unaTransferencia.getMonto();
        String fecha = unaTransferencia.getFecha().toString();

        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            String sql = "INSERT INTO transferencias (cbucuentaorigen, cbucuentadestino, moneda, monto, fecha) VALUES ('"
                    + cbuOrigen + "', '" + cbuDestino + "', '" + moneda + "', " + monto + ", '" + fecha + "')";
            s.executeUpdate(sql);
            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) { }
            throw new DAOException("Error al crear la transferencia", e);
        } finally {
            cerrarConexion(c);
        }
    }

    public Transferencia muestraTransferencia(int idTransferencia) throws DAOException {
        String sql = "SELECT * FROM transferencias WHERE id = " + idTransferencia;
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                return mapearTransferencia(rs);
            } else {
                throw new ObjetoNoEncontradoException("No existe una transferencia con id: " + idTransferencia);
            }
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            throw new DAOException("Error al obtener la transferencia con id: " + idTransferencia, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public List<Transferencia> listaTransferenciasUsuario(int dniUsuario) throws DAOException {
        String sql = "SELECT t.* FROM transferencias t "
                   + "JOIN cuentas c ON t.cbucuentaorigen = c.cbu OR t.cbucuentadestino = c.cbu "
                   + "WHERE c.dniusuario = " + dniUsuario;
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            List<Transferencia> transferencias = new ArrayList<>();
            while (rs.next()) {
                transferencias.add(mapearTransferencia(rs));
            }
            return transferencias;
        } catch (SQLException e) {
            throw new DAOException("Error al listar las transferencias del usuario con DNI: " + dniUsuario, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public List<Transferencia> listaTransferenciasCuenta(String cbu) throws DAOException {
        String sql = "SELECT * FROM transferencias "
                   + "WHERE cbucuentaorigen = '" + cbu + "' OR cbucuentadestino = '" + cbu + "' "
                   + "ORDER BY fecha DESC";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            List<Transferencia> transferencias = new ArrayList<>();
            while (rs.next()) {
                transferencias.add(mapearTransferencia(rs));
            }
            return transferencias;
        } catch (SQLException e) {
            throw new DAOException("Error al listar las transferencias de la cuenta: " + cbu, e);
        } finally {
            cerrarConexion(c);
        }
    }

    private Transferencia mapearTransferencia(ResultSet rs) throws SQLException {
        return new Transferencia(
                rs.getInt("id"),
                rs.getString("cbucuentaorigen"),
                rs.getString("cbucuentadestino"),
                rs.getString("moneda"),
                rs.getInt("monto"),
                rs.getTimestamp("fecha")
        );
    }

}
