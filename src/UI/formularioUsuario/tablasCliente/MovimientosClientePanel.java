package UI.formularioUsuario.tablasCliente;

import UI.formularioUsuario.CamposPanel;
import entidades.MovimientoTarjeta;
import exceptions.serviceExceptions.ServiceException;
import service.MovimientoTarjetaService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientosClientePanel extends CamposPanel {

    private static final String[] COLUMNAS = { "Fecha", "Referencia", "Monto ($)" };
    private static final String TITULO_SIN_SELECCION = "Movimientos — (seleccione una tarjeta)";
    private static final String TITULO_CON_TARJETA   = "Movimientos de tarjeta: ";

    private final MovimientoTarjetaService movimientoService;

    private TablaConAccionPanel tablaPanel;
    private JTextField campoMonto;
    private JTextField campoReferencia;
    private JButton botonRegistrar;

    private String numeroTarjetaActual;

    public MovimientosClientePanel(MovimientoTarjetaService movimientoService) {
        this.movimientoService = movimientoService;
        armarFormulario();
    }

    @Override
    public void armarFormulario() {
        this.setLayout(new BorderLayout());

        campoMonto = new JTextField(8);
        campoReferencia = new JTextField(20);
        botonRegistrar = new JButton("Registrar débito");

        campoMonto.setEnabled(false);
        campoReferencia.setEnabled(false);
        botonRegistrar.setEnabled(false);

        botonRegistrar.addActionListener(e -> registrarDebito());

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelSur.add(new JLabel("Monto ($):"));
        panelSur.add(campoMonto);
        panelSur.add(new JLabel("Referencia:"));
        panelSur.add(campoReferencia);
        panelSur.add(botonRegistrar);

        tablaPanel = new TablaConAccionPanel(TITULO_SIN_SELECCION, COLUMNAS, panelSur);
        this.add(tablaPanel, BorderLayout.CENTER);
    }

    public void cargarTarjeta(String numeroTarjeta) {
        this.numeroTarjetaActual = numeroTarjeta;
        actualizarTitulo(TITULO_CON_TARJETA + numeroTarjeta);
        habilitarFormulario(true);
        recargarTabla();
    }

    public void limpiar() {
        this.numeroTarjetaActual = null;
        actualizarTitulo(TITULO_SIN_SELECCION);
        habilitarFormulario(false);
        tablaPanel.recargarFilas(new ArrayList<>());
    }

    private void registrarDebito() {
        String montoTexto = campoMonto.getText().trim();
        String referencia = campoReferencia.getText().trim();

        int monto;
        try {
            monto = Integer.parseInt(montoTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El monto debe ser un número entero.",
                    "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            movimientoService.registrarMovimiento(numeroTarjetaActual, monto, referencia);
            campoMonto.setText("");
            campoReferencia.setText("");
            recargarTabla();
            JOptionPane.showMessageDialog(this,
                    "Débito de $" + monto + " registrado correctamente.",
                    "Movimiento registrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recargarTabla() {
        try {
            List<MovimientoTarjeta> movimientos =
                    movimientoService.listarMovimientosPorTarjeta(numeroTarjetaActual);
            List<Object[]> filas = new ArrayList<>();
            for (MovimientoTarjeta m : movimientos) {
                filas.add(new Object[]{ m.getFecha(), m.getReferencia(), m.getMonto() });
            }
            tablaPanel.recargarFilas(filas);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los movimientos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void habilitarFormulario(boolean habilitar) {
        campoMonto.setEnabled(habilitar);
        campoReferencia.setEnabled(habilitar);
        botonRegistrar.setEnabled(habilitar);
    }

    private void actualizarTitulo(String titulo) {
        tablaPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP));
    }
}
