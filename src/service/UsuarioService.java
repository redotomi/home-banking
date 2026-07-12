package service;

import dao.UsuarioDAO;
import entidades.Usuario;
import exceptions.DAOExceptions.ConexionDAOException;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoDuplicadoException;
import exceptions.DAOExceptions.ObjetoNoEncontradoException;
import exceptions.serviceExceptions.DniDuplicadoException;
import exceptions.serviceExceptions.ServiceException;
import exceptions.serviceExceptions.UsuarioNoEncontradoException;

import java.util.List;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;
    private CuentaService cuentaService;

    public UsuarioService(UsuarioDAO usuarioDAO, CuentaService cuentaService) {
        this.usuarioDAO = usuarioDAO;
        this.cuentaService = cuentaService;
    }

    public void agregarUsuario(Usuario usuario) throws ServiceException {
        try {
            this.usuarioDAO.muestraUsuario(usuario.getDni());
            throw new DniDuplicadoException(usuario.getDni());
        } catch (ObjetoNoEncontradoException e) {
            try {
                this.usuarioDAO.crearUsuario(usuario);
                this.cuentaService.crearCuenta(usuario, "Caja Ahorro Pesos");
            } catch (ObjetoDuplicadoException ex) {
                throw new DniDuplicadoException(usuario.getDni());
            } catch (DAOException ex) {
                throw new ServiceException("Error al agregar el usuario", ex);
            }
        } catch (DniDuplicadoException e) {
            throw e;
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
        } catch (ObjetoNoEncontradoException e) {
            throw new UsuarioNoEncontradoException(dniUsuario);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar el usuario", e);
        }
    }

    public void actualizarUsuario(Usuario usuario) throws ServiceException {
        try {
            try {
                Usuario existente = this.usuarioDAO.muestraUsuario(usuario.getDni());
                if (existente.getId() != usuario.getId()) {
                    throw new DniDuplicadoException(usuario.getDni());
                }
            } catch (ObjetoNoEncontradoException e) {}
						
            this.usuarioDAO.actualizarUsuario(usuario);
        } catch (DniDuplicadoException e) {
            throw e;
        } catch (ObjetoNoEncontradoException e) {
            throw new UsuarioNoEncontradoException("No existe el usuario a actualizar");
        } catch (ObjetoDuplicadoException e) {
            throw new DniDuplicadoException(usuario.getDni());
        } catch (DAOException e) {
            throw new ServiceException("Error al actualizar el usuario", e);
        }
    }

    public void eliminarUsuario(int dni) throws ServiceException {
        try {
            this.usuarioDAO.borrarUsuario(dni);
        } catch (ObjetoNoEncontradoException e) {
            throw new UsuarioNoEncontradoException(dni);
        } catch (DAOException e) {
            throw new ServiceException("Error al eliminar el usuario con DNI: " + dni, e);
        }
    }
}
