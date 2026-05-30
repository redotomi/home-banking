package dao.impl;

import dao.UsuarioDAO;
import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoDuplicadoException;
import util.DBManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImplH2 implements UsuarioDAO {
    public void crearUsuario(Usuario unUsuario) throws DAOException {
        String nombre = unUsuario.getNombre();
        String apellido = unUsuario.getApellido();
        int dni = unUsuario.getDni();

        Connection c = DBManager.connect();
        try {
            Statement s = c.createStatement();
            String sql = "INSERT INTO usuarios (nombre, apellido, dni) VALUES ('" + nombre + "', '" + apellido + "', '" + dni + "' )";
            s.executeUpdate(sql);
            c.commit();
        } catch (SQLException e) {
            if (e.getErrorCode() == 27001) {
                throw new ObjetoDuplicadoException("No se puedo insertar el paciente");
            }
            if (e.getErrorCode() == 23505) {
                throw new ObjetoDuplicadoException("El nombre ya esta en uso");
            }
            try {
                e.printStackTrace();
                c.rollback();
            } catch (SQLException el) {
                throw new DAOException("no se pudo insertar");
            }
        }
        finally {
            try {
                c.close();
            } catch (SQLException el) {
                el.printStackTrace();
            }
        }
    }


    public void borrarUsuario(int dni) throws DAOException {
        String sql = "DELETE FROM usuarios WHERE dni = '" + dni + "'";
        Connection c = DBManager.connect();
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
                e.printStackTrace();
            } catch (SQLException el) {
                el.printStackTrace();
            }
        } finally {
            try {
                c.close();
            } catch (SQLException el) {
                el.printStackTrace();
            }
        }

    }


    public void actualizarUsuario(Usuario unUsuario) {
        String nombre = unUsuario.getNombre();
        String apellido = unUsuario.getApellido();
        int dni = unUsuario.getDni();

        String sql = "UPDATE usuarios set apellido = '" + apellido + "', dni = '" + dni + "' WHERE nombre = '" + nombre + "'";
        Connection c = DBManager.connect();
        try {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
                e.printStackTrace();
            } catch (SQLException el) {
                el.printStackTrace();
            }
        } finally {
            try {
                c.close();
            } catch (SQLException el) {
                el.printStackTrace();
            }
        }

    }

    /*para crear, editar o borrar se usa el .executeUpdate()
     * y para mostrar/consultar algo a la base de datos, uso el .executeQuery()*/
    public Usuario muestraUsuario(int dni) {
        String sql = "SELECT * FROM usuarios WHERE dni = '" + dni + "'";
        Connection c = DBManager.connect();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql); //me devuele un ResultSet, que contiene las tuplas del resultado


            /*obtengo el resultado y las tuplas almacenadas en la var rs. luego tegno que recorrer el rs, porque es un conjunto de datos sueltos q vienen de la BD. arranca con un puntero en una posicion antes del resultado, y el next() va moviendo ese puntero y si no hya mas resultados, devuelve false. y esto lo puedo usar para un bucle  */
            if (rs.next()) { // si hay resultados, entra en el if
                int id = rs.getInt("id");
                String nombreUsuario = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int dniUsuario = rs.getInt("dni");
                Usuario p = new Usuario(id, nombreUsuario, apellido, dniUsuario);
                return p;
            }
        } catch (SQLException e) { // aca tengo que poner mis propias exceptions
            try {
                c.rollback();
                e.printStackTrace();
            } catch (SQLException el) {
                el.printStackTrace();
            }
        } finally {
            try {
                c.close(); // siempre tengo qeu cerrar la conexion a la BD
            } catch (SQLException el) {
                el.printStackTrace();
            }

        }

        return null;
    }


    public List<Usuario> listaTodosLosUsuarios() throws DAOException {
        List<Usuario> resultado = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        Connection c = DBManager.connect();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) { //mientras haya mas resultados, repite este codigo
                int id = rs.getInt("id");
                String user = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int dni = rs.getInt("dni");
                Usuario p = new Usuario(id, user, apellido, dni);
                resultado.add(p); // por cada usuario nuevo que encuentro, lo guardo en esta lista
            }
        } catch (SQLException e) {
            try {
                c.rollback();
                e.printStackTrace();
            } catch (SQLException el) {
                throw new DAOException("error al listar usuarios");
            }
        } finally {
            try {
                c.close();
            } catch (SQLException el) {
                el.printStackTrace();
            }
        }

        return resultado;
    }
}
