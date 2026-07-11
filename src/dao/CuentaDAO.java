package dao;

import entidades.Cuenta;
import exceptions.DAOExceptions.DAOException;

import java.util.List;

public interface CuentaDAO {
    void crearCuenta(Cuenta unaCuenta, int dni) throws DAOException;

    void borrarCuenta(String cbu) throws DAOException;

    void actualizarCuenta(Cuenta unaCuenta) throws DAOException;

    void actualizarSaldo(String cbu, int nuevoSaldo) throws DAOException;

    Cuenta muestraCuenta(String cbu) throws DAOException;

    List<Cuenta> listaTodasLasCuentas(int dni) throws DAOException;
}
