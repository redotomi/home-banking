package UI.formularioUsuario.tablasCliente;

import entidades.Cuenta;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.CuentaService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CuentasClientePanel extends AbstractTablaClientePanel {

    private static final String[] TIPOS_CUENTA = {
        "Caja Ahorro Pesos",
        "Caja Ahorro Dólares",
        "Cuenta Corriente Pesos",
        "Cuenta Corriente Dólares"
    };

    private static final String[] COLUMNAS = { "Tipo de cuenta", "Moneda", "Saldo" };

    private final Usuario cliente;
    private final CuentaService cuentaService;

    public CuentasClientePanel(Usuario cliente, CuentaService cuentaService) {
        this.cliente       = cliente;
        this.cuentaService = cuentaService;
        armarFormulario();
    }

    @Override protected String getTitulo()          { return "Cuentas"; }
    @Override protected String[] getColumnas()      { return COLUMNAS; }
    @Override protected String[] getOpcionesCombo() { return TIPOS_CUENTA; }
    @Override protected String getLabelCombo()      { return "Tipo:"; }
    @Override protected String getLabelBoton()      { return "Agregar cuenta"; }

    @Override
    protected List<Object[]> obtenerFilas() throws ServiceException {
        List<Cuenta> cuentas = cuentaService.listarCuentasDeCliente(cliente.getDni());
        List<Object[]> filas = new ArrayList<>();
        for (Cuenta c : cuentas) {
            filas.add(new Object[]{ c.getNombreCuenta(), c.getMoneda(), c.getMonto() });
        }
        return filas;
    }

    @Override
    protected void agregarEntidad(String tipoCuenta) {
        try {
            cuentaService.crearCuenta(cliente, tipoCuenta);
            recargarTabla();
            JOptionPane.showMessageDialog(this,
                    "Cuenta \"" + tipoCuenta + "\" agregada correctamente.",
                    "Cuenta creada", JOptionPane.INFORMATION_MESSAGE);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear la cuenta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
