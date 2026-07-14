package dao.impl;

import dao.CuentaDAO;
import entidades.Cuenta;
import exceptions.DAOExceptions.ConexionDAOException;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoDuplicadoException;
import exceptions.DAOExceptions.ObjetoNoEncontradoException;
import util.DBManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAOImplH2 extends AbstractDAO implements CuentaDAO {
	public void crearCuenta(Cuenta unaCuenta, int dni) throws DAOException {
		String nombreCuenta = unaCuenta.getNombreCuenta();
		String cbu = unaCuenta.getCBU();
		String alias = unaCuenta.getAlias();
		String moneda = unaCuenta.getMoneda();
		int monto = unaCuenta.getMonto();
		
		Connection c = obtenerConexion();
		try {
			Statement s = c.createStatement();
			String sql = "INSERT INTO cuentas (nombre, cbu, alias, moneda, monto, dniusuario) VALUES ('"
					+ nombreCuenta + "', '" + cbu + "', '" + alias + "', '" + moneda + "', '" + monto + "', '" + dni + "')";
			s.executeUpdate(sql);
			c.commit();
		} catch (SQLException e) {
				try { c.rollback(); } catch (SQLException ex) { }

				if (e.getErrorCode() == 23505 || e.getErrorCode() == 27001) {
					throw new ObjetoDuplicadoException("Ya existe una cuenta con ese CBU", e);
				}
				throw new DAOException("Error al crear la cuenta", e);
		} finally {
			cerrarConexion(c);
		}
	}
	
	public void borrarCuenta(String cbu) throws DAOException {
		String sql = "DELETE FROM cuentas WHERE cbu = '" + cbu + "'";
		Connection c = obtenerConexion();
		try {
			Statement s = c.createStatement();
			int filasBorradas = s.executeUpdate(sql);
			if (filasBorradas == 0) {
				throw new ObjetoNoEncontradoException("No existe una cuenta con CBU: " + cbu);
			}
			c.commit();
		} catch (ObjetoNoEncontradoException e) {
			throw e;
		} catch (SQLException e) {
			try { c.rollback(); } catch (SQLException ex) { }
			throw new DAOException("Error al eliminar la cuenta con CBU: " + cbu, e);
		} finally {
			cerrarConexion(c);
		}
	}

	public void actualizarCuenta(Cuenta unaCuenta) throws DAOException {
		String nombreCuenta = unaCuenta.getNombreCuenta();
		String cbu = unaCuenta.getCBU();
		String alias = unaCuenta.getAlias();
		String moneda = unaCuenta.getMoneda();
		int monto = unaCuenta.getMonto();
		
		Connection c = obtenerConexion();
		try {
			Statement s = c.createStatement();
			String sql = "UPDATE cuentas SET nombre = '" + nombreCuenta + "', cbu = '" + cbu + "', alias = '" + alias + "', moneda = '" + moneda + "', monto = '" + monto + "' WHERE cbu = '" + cbu + "'";
			int filasActualizadas = s.executeUpdate(sql);
			if (filasActualizadas == 0) {
				throw new ObjetoNoEncontradoException("No existe una cuenta con CBU: " + cbu);
			}
			c.commit();
		} catch (ObjetoNoEncontradoException e) {
			throw e;
		} catch (SQLException e) {
			try { c.rollback(); } catch (SQLException ex) { }
			if (e.getErrorCode() == 23505) {
				throw new ObjetoDuplicadoException("Ya existe una cuenta con ese CBU", e);
			}
			throw new DAOException("Error al actualizar la cuenta", e);
		} finally {
			cerrarConexion(c);
		}
	}

	public void actualizarSaldo(String cbu, int nuevoSaldo) throws DAOException {
		String sql = "UPDATE cuentas SET monto = '" + nuevoSaldo + "' WHERE cbu = '" + cbu + "'";
		Connection c = obtenerConexion();
		try {
			Statement s = c.createStatement();
			int filasActualizadas = s.executeUpdate(sql);
			if (filasActualizadas == 0) {
				throw new ObjetoNoEncontradoException("No existe una cuenta con CBU: " + cbu);
			}
			c.commit();
		} catch (ObjetoNoEncontradoException e) {
			throw e;
		} catch (SQLException e) {
			try { c.rollback(); } catch (SQLException ex) { }
			if (e.getErrorCode() == 23505) {
				throw new ObjetoDuplicadoException("Ya existe una cuenta con ese CBU", e);
			}
			throw new DAOException("Error al actualizar el saldo de la cuenta con CBU: " + cbu, e);
		} finally {
			cerrarConexion(c);
		}
	}

	public Cuenta muestraCuenta(String cbu) throws DAOException {
		String sql = "SELECT * FROM cuentas WHERE cbu = '" + cbu + "'";
		Connection c = obtenerConexion();
		try {
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				return new Cuenta(rs.getString("nombre"), rs.getString("cbu"), rs.getString("alias"), rs.getString("moneda"), rs.getInt("monto"));
			} else {
				throw new ObjetoNoEncontradoException("No existe una cuenta con CBU: " + cbu);
			}
		} catch (SQLException e) {
			throw new DAOException("Error al mostrar la cuenta con CBU: " + cbu, e);
		} finally {
			cerrarConexion(c);
		}		
	}

	public Cuenta muestraCuentaPorAlias(String alias) throws DAOException {
		String sql = "SELECT * FROM cuentas WHERE alias = '" + alias + "'";
		Connection c = obtenerConexion();
		try {
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				return new Cuenta(rs.getString("nombre"), rs.getString("cbu"), rs.getString("alias"), rs.getString("moneda"), rs.getInt("monto"));
			} else {
				throw new ObjetoNoEncontradoException("No existe una cuenta con alias: " + alias);
			}
		} catch (SQLException e) {
			throw new DAOException("Error al mostrar la cuenta con alias: " + alias, e);
		} finally {
			cerrarConexion(c);
		}
	}

	public List<Cuenta> listaTodasLasCuentas(int dni) throws DAOException {
		String sql = "SELECT * FROM cuentas WHERE dniusuario = '" + dni + "'";
		Connection c = obtenerConexion();
		try {
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			List<Cuenta> cuentas = new ArrayList<>();
			while (rs.next()) {
				Cuenta cuenta = new Cuenta(rs.getString("nombre"), rs.getString("cbu"), rs.getString("alias"), rs.getString("moneda"), rs.getInt("monto"));
				cuentas.add(cuenta);
			}
			return cuentas;
		} catch (SQLException e) {
			throw new DAOException("Error al listar las cuentas del usuario con DNI: " + dni, e);
		} finally {
			cerrarConexion(c);
		}
	}
	
}