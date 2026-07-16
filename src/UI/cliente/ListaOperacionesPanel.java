package UI.cliente;

import UI.PanelManager;
import entidades.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class ListaOperacionesPanel extends JPanel {

    protected final PanelManager panelManager;
    protected final Cliente cliente;

    protected ListaOperacionesPanel(PanelManager panelManager, Cliente cliente) {
        this.panelManager = panelManager;
        this.cliente = cliente;
    }

    protected void armarPanel() {
        this.setLayout(new BorderLayout(0, 12));
        this.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        this.add(buildEncabezado(), BorderLayout.NORTH);
        this.add(buildTabla(), BorderLayout.CENTER);
        this.add(buildPie(), BorderLayout.SOUTH);
    }

    private JPanel buildEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JLabel titulo = new JLabel(getTitulo());
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));
        panel.add(titulo, BorderLayout.WEST);

        return panel;
    }

    private JScrollPane buildTabla() {
        DefaultTableModel modelo = new DefaultTableModel(getFilas(), getColumnas()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    protected JPanel buildPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener(e -> panelManager.mostrarPantallaCliente(cliente));
        panel.add(botonVolver);
        return panel;
    }

    protected abstract String getTitulo();

    protected abstract String[] getColumnas();

    protected abstract Object[][] getFilas();
}
