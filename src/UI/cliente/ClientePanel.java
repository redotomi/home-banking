package UI.cliente;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Cuenta;
import exceptions.serviceExceptions.ServiceException;
import service.CuentaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientePanel extends JPanel {

    private final PanelManager panelManager;
    private final Cliente cliente;
    private final CuentaService cuentaService;

    public ClientePanel(PanelManager panelManager, Cliente cliente, CuentaService cuentaService) {
        super(new BorderLayout(0, 0));
        this.panelManager = panelManager;
        this.cliente = cliente;
        this.cuentaService = cuentaService;
        armarPanel();
    }

    private void armarPanel() {
        this.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel encabezado = buildEncabezado();
        this.add(encabezado, BorderLayout.NORTH);

        JPanel cuerpo = buildCuerpo();
        this.add(cuerpo, BorderLayout.CENTER);

        JPanel pie = buildPie();
        this.add(pie, BorderLayout.SOUTH);
    }

    private JPanel buildEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JLabel bienvenida = new JLabel(
                "Bienvenido/a, " + cliente.getNombre() + " " + cliente.getApellido(),
                SwingConstants.LEFT
        );
        bienvenida.setFont(bienvenida.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(bienvenida, BorderLayout.WEST);

        return panel;
    }

    private JPanel buildCuerpo() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        JLabel titulo = new JLabel("Mis cuentas");
        titulo.setFont(titulo.getFont().deriveFont(Font.PLAIN, 13f));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panel.add(titulo, BorderLayout.NORTH);

        List<Cuenta> cuentas = cargarCuentas();
        ListaCuentasPanel listaCuentasPanel = new ListaCuentasPanel(cuentas);

        JScrollPane scroll = new JScrollPane(listaCuentasPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));

        JButton botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.addActionListener(e -> panelManager.mostrarLoginPanel());
        panel.add(botonCerrarSesion);

        return panel;
    }

    private List<Cuenta> cargarCuentas() {
        try {
            return cuentaService.listarCuentasDeCliente(cliente.getDni());
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar las cuentas: " + e.getMessage(),
                    "Error del sistema", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
}
