package dao;

import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;

import java.util.List;

public interface UsuarioDAO {
    void crearUsuario(Usuario unUsuario) throws DAOException;

    void borrarUsuario(String user);

    void actualizarUsuario(Usuario unUsuario) throws DAOException;

    Usuario muestraUsuario(String user);

    List<Usuario> listaTodosLosUsuarios() throws DAOException;

}
