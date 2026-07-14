package UI.formularioUsuario.tablasCliente;

import entidades.Tarjeta;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.TarjetaService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TarjetasClientePanel extends AbstractTablaClientePanel {

    private static final String[] PROVEEDORES   = { "VISA", "MC", "AMEX" };
    private static final String[] COLUMNAS      = { "Número", "Proveedor", "Límite", "Vencimiento" };
    private static final int      LIMITE_DEFAULT = 2_000_000;

    private final Usuario cliente;
    private final TarjetaService tarjetaService;

    public TarjetasClientePanel(Usuario cliente, TarjetaService tarjetaService) {
        this.cliente        = cliente;
        this.tarjetaService = tarjetaService;
        armarFormulario();
    }

    @Override protected String getTitulo()          { return "Tarjetas"; }
    @Override protected String[] getColumnas()      { return COLUMNAS; }
    @Override protected String[] getOpcionesCombo() { return PROVEEDORES; }
    @Override protected String getLabelCombo()      { return "Proveedor:"; }
    @Override protected String getLabelBoton()      { return "Agregar tarjeta"; }

    @Override
    protected List<Object[]> obtenerFilas() throws ServiceException {
        List<Tarjeta> tarjetas = tarjetaService.listarTarjetasDeCliente(String.valueOf(cliente.getDni()));
        List<Object[]> filas = new ArrayList<>();
        for (Tarjeta t : tarjetas) {
            filas.add(new Object[]{ t.getNumero(), t.getProveedor(), t.getLimite(), t.getVencimiento() });
        }
        return filas;
    }

    /**
     * Devuelve el número de la tarjeta actualmente seleccionada en la tabla,
     * o {@code null} si no hay ninguna selección.
     */
    public String getNumeroTarjetaSeleccionada() {
        int fila = getTabla().getSelectedRow();
        if (fila < 0) return null;
        return (String) getTabla().getValueAt(fila, 0);
    }

    @Override
    protected void agregarEntidad(String proveedor) {
        try {
            tarjetaService.crearTarjeta(cliente, proveedor, LIMITE_DEFAULT);
            recargarTabla();
            JOptionPane.showMessageDialog(this,
                    "Tarjeta " + proveedor + " creada correctamente.",
                    "Tarjeta creada", JOptionPane.INFORMATION_MESSAGE);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear la tarjeta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
