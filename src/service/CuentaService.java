package service;

import dao.CuentaDAO;
import entidades.Cuenta;
import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;
import exceptions.serviceExceptions.ServiceException;

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
