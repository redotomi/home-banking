package UI.formularioUsuario.tablasCliente;

import UI.formularioUsuario.CamposPanel;

import exceptions.serviceExceptions.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class AbstractTablaClientePanel extends CamposPanel {

    private TablaConAccionPanel tablaPanel;
    protected JComboBox<String> combo;

    @Override
    public final void armarFormulario() {
        this.setLayout(new BorderLayout());

        combo = new JComboBox<>(getOpcionesCombo());

        JButton boton = new JButton(getLabelBoton());
        boton.addActionListener(e -> agregarEntidad((String) combo.getSelectedItem()));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelSur.add(new JLabel(getLabelCombo()));
        panelSur.add(combo);
        panelSur.add(boton);

        tablaPanel = new TablaConAccionPanel(getTitulo(), getColumnas(), panelSur);
        this.add(tablaPanel, BorderLayout.CENTER);

        recargarTabla();
    }

    protected void recargarTabla() {
        try {
            tablaPanel.recargarFilas(obtenerFilas());
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected abstract String getTitulo();

    protected abstract String[] getColumnas();

    protected abstract String[] getOpcionesCombo();

    protected abstract String getLabelCombo();

    protected abstract String getLabelBoton();

    protected abstract List<Object[]> obtenerFilas() throws ServiceException;

    protected abstract void agregarEntidad(String seleccion);

    public JTable getTabla() {
        return tablaPanel.getTabla();
    }
}
