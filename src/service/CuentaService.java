package service;

import dao.CuentaDAO;
import entidades.Cuenta;
import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;
import exceptions.DAOExceptions.ObjetoNoEncontradoException;
import exceptions.serviceExceptions.CuentaNoEncontradaException;
import exceptions.serviceExceptions.ServiceException;

import java.util.List;
import java.util.Random;

public class CuentaService {
    private static final String NOMBRE_CUENTA_DEFAULT = "Caja Ahorro Pesos";
    private static final String MONEDA_DEFAULT = "ARS";
    private static final int LONGITUD_CBU = 22;

    private CuentaDAO cuentaDAO;

    public CuentaService(CuentaDAO cuentaDAO) {
        this.cuentaDAO = cuentaDAO;
    }


    public void crearCuentaParaUsuario(Usuario usuario) throws ServiceException {
        String cbu = generarCBU();
        String alias = construirAlias(usuario.getNombre(), usuario.getApellido(), cbu);
        Cuenta cuenta = new Cuenta(NOMBRE_CUENTA_DEFAULT, cbu, alias, MONEDA_DEFAULT, 0);
        try {
            cuentaDAO.crearCuenta(cuenta, usuario.getDni());
        } catch (DAOException e) {
            throw new ServiceException("Error al crear la cuenta para el usuario con DNI: " + usuario.getDni(), e);
        }
    }

    public List<Cuenta> listarCuentasDeCliente(int dni) throws ServiceException {
        try {
            return cuentaDAO.listaTodasLasCuentas(dni);
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener las cuentas del cliente con DNI: " + dni, e);
        }
    }

    public Cuenta muestraCuenta(String cbu) throws ServiceException {
        try {
            return cuentaDAO.muestraCuenta(cbu);
        } catch (ObjetoNoEncontradoException e) {
            throw new CuentaNoEncontradaException(cbu);
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener la cuenta con CBU: " + cbu, e);
        }
    }

    public void actualizarSaldo(String cbu, int nuevoSaldo) throws ServiceException {
        try {
            cuentaDAO.actualizarSaldo(cbu, nuevoSaldo);
        } catch (DAOException e) {
            throw new ServiceException("Error al actualizar el saldo de la cuenta con CBU: " + cbu, e);
        }
    }

    private String generarCBU() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LONGITUD_CBU);

        sb.append(1 + random.nextInt(9));
        for (int i = 1; i < LONGITUD_CBU; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String construirAlias(String nombre, String apellido, String cbu) {
        String ultimos6 = cbu.substring(cbu.length() - 6);
        return (nombre + "." + apellido + "." + ultimos6).toLowerCase();
    }
}
