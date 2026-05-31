package dao;

import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;

import java.util.List;

public interface UsuarioDAO {
    void crearUsuario(Usuario unUsuario) throws DAOException;

    void borrarUsuario(int dni) throws DAOException;

    void actualizarUsuario(Usuario unUsuario) throws DAOException;

    Usuario muestraUsuario(int dni) throws DAOException;

    List<Usuario> listaTodosLosUsuarios() throws DAOException;
}
