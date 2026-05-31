package service;

import dao.UsuarioDAO;
import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoDuplicadoException;
import exceptions.serviceExceptions.DniDuplicadoException;
import exceptions.serviceExceptions.ServiceException;

import java.util.List;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void agregarUsuario(Usuario usuario) throws ServiceException {
        try {
            Usuario existente = this.usuarioDAO.muestraUsuario(usuario.getDni());
            if (existente != null) {
                throw new DniDuplicadoException(usuario.getDni());
            }
            this.usuarioDAO.crearUsuario(usuario);
        } catch (DniDuplicadoException e) {
            throw e;
        } catch (ObjetoDuplicadoException e) {
            throw new DniDuplicadoException(usuario.getDni());
        } catch (DAOException e) {
            throw new ServiceException("Error al agregar el usuario", e);
        }
    }

    public List<Usuario> consultarUsuarios() throws ServiceException {
        try {
            return this.usuarioDAO.listaTodosLosUsuarios();
        } catch (DAOException e) {
            throw new ServiceException("Error al consultar los usuarios", e);
        }
    }

    public Usuario buscarUsuario(int dniUsuario) throws ServiceException {
        try {
            return this.usuarioDAO.muestraUsuario(dniUsuario);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar el usuario", e);
        }
    }

    public void actualizarUsuario(Usuario usuario) throws ServiceException {
        try {
            Usuario existente = this.usuarioDAO.muestraUsuario(usuario.getDni());
            if (existente != null && existente.getId() != usuario.getId()) {
                throw new DniDuplicadoException(usuario.getDni());
            }
            this.usuarioDAO.actualizarUsuario(usuario);
        } catch (DniDuplicadoException e) {
            throw e;
        } catch (ObjetoDuplicadoException e) {
            throw new DniDuplicadoException(usuario.getDni());
        } catch (DAOException e) {
            throw new ServiceException("Error al actualizar el usuario", e);
        }
    }

    public void eliminarUsuario(int dni) throws ServiceException {
        try {
            this.usuarioDAO.borrarUsuario(dni);
        } catch (DAOException e) {
            throw new ServiceException("Error al eliminar el usuario con DNI: " + dni, e);
        }
    }
}
