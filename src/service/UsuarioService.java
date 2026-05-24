package service;

import dao.UsuarioDAO;
import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoDuplicadoException;
import exceptions.serviceExceptions.ServiceException;

import java.util.List;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void agregarUsuario(Usuario paciente) throws ServiceException {
        try {
            this.usuarioDAO.crearUsuario(paciente);
        } catch (ObjetoDuplicadoException e) {
            throw new ServiceException("GRAVE", e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public List<Usuario> consultarUsuarios() throws ServiceException {
        try {
            return this.usuarioDAO.listaTodosLosUsuarios();
        } catch (DAOException e) {
            throw new ServiceException();
        }

    }

    public void actualizarUsuario(Usuario usuario) throws ServiceException {
        try {
            this.usuarioDAO.actualizarUsuario(usuario);
            System.out.println("Usuario: " + usuario.getNombre() + " modificado correctamente!");
        } catch (DAOException e) {
            throw new ServiceException();
        }
    }
}
