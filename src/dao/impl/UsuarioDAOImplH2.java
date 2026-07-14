package dao.impl;

import dao.UsuarioDAO;
import entidades.Administrador;
import entidades.Cliente;
import entidades.Usuario;
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

public class UsuarioDAOImplH2 extends AbstractDAO implements UsuarioDAO {

    public void crearUsuario(Usuario unUsuario) throws DAOException {
        String nombre = unUsuario.getNombre();
        String apellido = unUsuario.getApellido();
        int dni = unUsuario.getDni();
        String rol = "CLIENTE";
        if (unUsuario instanceof Administrador) {
            rol = "ADMIN";
        }

        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            String sql = "INSERT INTO usuarios (nombre, apellido, dni, rol) VALUES ('"
                    + nombre + "', '" + apellido + "', '" + dni + "', '" + rol + "')";
            s.executeUpdate(sql);
            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) { }
            // H2: 23505 = unique constraint violation (DNI duplicado)
            if (e.getErrorCode() == 23505 || e.getErrorCode() == 27001) {
                throw new ObjetoDuplicadoException("Ya existe un usuario con ese DNI", e);
            }
            throw new DAOException("Error al crear el usuario", e);
        } finally {
            cerrarConexion(c);
        }
    }

    public void borrarUsuario(int dni) throws DAOException {
        String sql = "DELETE FROM usuarios WHERE dni = '" + dni + "'";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            int filasBorradas = s.executeUpdate(sql);
            if (filasBorradas == 0) {
                throw new ObjetoNoEncontradoException("No existe un usuario con DNI: " + dni);
            }
            c.commit();
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) { }
            throw new DAOException("Error al eliminar el usuario con DNI: " + dni, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public void actualizarUsuario(Usuario unUsuario) throws DAOException {
        String nombre = unUsuario.getNombre();
        String apellido = unUsuario.getApellido();
        int dni = unUsuario.getDni();

        String sql = "UPDATE usuarios SET nombre = '" + nombre
                + "', apellido = '" + apellido
                + "', dni = '" + dni + "' WHERE dni = " + unUsuario.getDni();
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            int filasActualizadas = s.executeUpdate(sql);
            if (filasActualizadas == 0) {
                throw new ObjetoNoEncontradoException("No existe un usuario con nombre: " + nombre);
            }
            c.commit();
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) { }
            if (e.getErrorCode() == 23505) {
                throw new ObjetoDuplicadoException("Ya existe un usuario con ese DNI", e);
            }
            throw new DAOException("Error al actualizar el usuario", e);
        } finally {
            cerrarConexion(c);
        }
    }

    /*para crear, editar o borrar se usa el .executeUpdate()
     * y para mostrar/consultar algo a la base de datos, uso el .executeQuery()*/
    public Usuario muestraUsuario(int dni) throws DAOException {
        String sql = "SELECT * FROM usuarios WHERE dni = '" + dni + "'";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql); //me devuelve un ResultSet, que contiene las tuplas del resultado

            /*obtengo el resultado y las tuplas almacenadas en la var rs. luego tengo que recorrer el rs, porque es un conjunto de datos sueltos q vienen de la BD. arranca con un puntero en una posicion antes del resultado, y el next() va moviendo ese puntero y si no hay mas resultados, devuelve false. y esto lo puedo usar para un bucle */
            if (rs.next()) { // si hay resultados, entra en el if
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int dniUsuario = rs.getInt("dni");
                String rol = rs.getString("rol");
                if (rol.equals("ADMIN")) {
                    return new Administrador(id, nombre, apellido, dniUsuario);
                } else {
                    return new Cliente(id, nombre, apellido, dniUsuario);
                }
            }
            throw new ObjetoNoEncontradoException("No existe un usuario con DNI: " + dni);
        } catch (ObjetoNoEncontradoException e) {
            throw e;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al buscar el usuario con DNI: " + dni, e);
        } finally {
            cerrarConexion(c);
        }
    }

    public List<Usuario> listaTodosLosUsuarios() throws DAOException {
        List<Usuario> resultado = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        Connection c = obtenerConexion();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) { //mientras haya mas resultados, repite este codigo
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int dni = rs.getInt("dni");
                String rol = rs.getString("rol");
                if (rol.equals("ADMIN")) {
                    resultado.add(new Administrador(id, nombre, apellido, dni));
                } else {
                    resultado.add(new Cliente(id, nombre, apellido, dni));
                }
            }
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            throw new DAOException("Error al listar los usuarios", e);
        } finally {
            cerrarConexion(c);
        }

        return resultado;
    }


}
