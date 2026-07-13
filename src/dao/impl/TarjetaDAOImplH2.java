package dao.impl;

import dao.TarjetaDAO;
import entidades.Tarjeta;
import exceptions.DAOExceptions.ConexionDAOException;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoDuplicadoException;
import exceptions.DAOExceptions.ObjetoNoEncontradoException;
import util.DBManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarjetaDAOImplH2 implements TarjetaDAO {

    public void crearTarjeta(Tarjeta tarjeta) throws DAOException {
        String sql = "INSERT INTO TARJETAS (numero, limite, consumo, proveedor, vencimiento, cvv, nombreTitular, dniUsuario) "
                   + "VALUES ('" + tarjeta.getNumero() + "', "
                   + tarjeta.getLimite() + ", "
                   + tarjeta.getConsumo() + ", '"
                   + tarjeta.getProveedor() + "', '"
                   + tarjeta.getVencimiento() + "', '"
                   + tarjeta.getCvv() + "', '"
                   + tarjeta.getNombreTitular() + "', '"
                   + tarjeta.getDniUsuario() + "')";

        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            if (e.getErrorCode() == 23505) {
                throw new ObjetoDuplicadoException("Ya existe una tarjeta con el número: " + tarjeta.getNumero(), e);
            }
            throw new DAOException("Error al crear la tarjeta", e);
        } finally {
            cerrarConexion(c);
        }
    }

    public void borrarTarjeta(String numero) throws DAOException {
        String sql = "DELETE FROM TARJETAS WHERE numero = '" + numero + "'";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            int filasBorradas = s.executeUpdate(sql);
            if (filasBorradas == 0) {
                throw new ObjetoNoEncontradoException("No existe una tarjeta con el número: " + numero);
            }
            c.commit();
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al eliminar la tarjeta con número: " + numero, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public void actualizarTarjeta(Tarjeta tarjeta) throws DAOException {
        String sql = "UPDATE TARJETAS SET "
                   + "limite = " + tarjeta.getLimite() + ", "
                   + "consumo = " + tarjeta.getConsumo() + ", "
                   + "proveedor = '" + tarjeta.getProveedor() + "', "
                   + "vencimiento = '" + tarjeta.getVencimiento() + "', "
                   + "cvv = '" + tarjeta.getCvv() + "', "
                   + "nombreTitular = '" + tarjeta.getNombreTitular() + "', "
                   + "dniUsuario = '" + tarjeta.getDniUsuario() + "' "
                   + "WHERE numero = '" + tarjeta.getNumero() + "'";

        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            int filasActualizadas = s.executeUpdate(sql);
            if (filasActualizadas == 0) {
                throw new ObjetoNoEncontradoException("No existe una tarjeta con el número: " + tarjeta.getNumero());
            }
            c.commit();
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al actualizar la tarjeta", e);
        } finally {
            cerrarConexion(c);
        }
    }

    public Tarjeta buscarTarjeta(String numero) throws DAOException {
        String sql = "SELECT * FROM TARJETAS WHERE numero = '" + numero + "'";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                return mapearTarjeta(rs);
            }
            throw new ObjetoNoEncontradoException("No existe una tarjeta con el número: " + numero);
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al buscar la tarjeta con número: " + numero, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public List<Tarjeta> listarTarjetasPorUsuario(String dniUsuario) throws DAOException {
        List<Tarjeta> resultado = new ArrayList<>();
        String sql = "SELECT * FROM TARJETAS WHERE dniUsuario = '" + dniUsuario + "'";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                resultado.add(mapearTarjeta(rs));
            }
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al listar las tarjetas del usuario: " + dniUsuario, e);
        } finally {
            cerrarConexion(c);
        }
        return resultado;
    }

    public List<Tarjeta> listarTodasLasTarjetas() throws DAOException {
        List<Tarjeta> resultado = new ArrayList<>();
        String sql = "SELECT * FROM TARJETAS";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                resultado.add(mapearTarjeta(rs));
            }
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al listar todas las tarjetas", e);
        } finally {
            cerrarConexion(c);
        }
        return resultado;
    }

    private Tarjeta mapearTarjeta(ResultSet rs) throws SQLException {
        String numero         = rs.getString("numero");
        int limite            = rs.getInt("limite");
        int consumo           = rs.getInt("consumo");
        String proveedor      = rs.getString("proveedor");
        LocalDate vencimiento = rs.getDate("vencimiento").toLocalDate();
        String cvv            = rs.getString("cvv");
        String nombreTitular  = rs.getString("nombreTitular");
        String dniUsuario     = rs.getString("dniUsuario");
        return new Tarjeta(numero, limite, consumo, proveedor, vencimiento, cvv, nombreTitular, dniUsuario);
    }

    private Connection obtenerConexion() throws ConexionDAOException {
        Connection c = DBManager.connect();
        if (c == null) {
            throw new ConexionDAOException("No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    private void cerrarConexion(Connection c) {
        try {
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
