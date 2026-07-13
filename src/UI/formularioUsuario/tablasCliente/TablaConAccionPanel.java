package UI.formularioUsuario.tablasCliente;

import UI.formularioUsuario.CamposPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TablaConAccionPanel extends CamposPanel {

    private final String titulo;
    private final String[] columnas;
    private final JPanel panelSur;

    private DefaultTableModel tableModel;
    private JTable tabla;

    public TablaConAccionPanel(String titulo, String[] columnas, JPanel panelSur) {
        this.titulo   = titulo;
        this.columnas = columnas;
        this.panelSur = panelSur;
        armarFormulario();
    }

    @Override
    public void armarFormulario() {
        this.setLayout(new BorderLayout(0, 8));
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP));

        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(tableModel);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 120));
        this.add(scroll, BorderLayout.CENTER);

        if (panelSur != null) {
            this.add(panelSur, BorderLayout.SOUTH);
        }
    }

    public void recargarFilas(List<Object[]> filas) {
        tableModel.setRowCount(0);
        for (Object[] fila : filas) {
            tableModel.addRow(fila);
        }
    }

    public JTable getTabla() {
        return tabla;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
