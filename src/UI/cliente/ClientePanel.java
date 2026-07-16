package UI.cliente;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Cuenta;
import entidades.Tarjeta;
import exceptions.serviceExceptions.ServiceException;
import service.CuentaService;
import service.TarjetaService;
import UI.cliente.cuentas.ListaCuentasPanel;
import UI.cliente.tarjetas.ListaTarjetasPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientePanel extends JPanel {

    private final PanelManager panelManager;
    private final Cliente cliente;
    private final CuentaService cuentaService;
    private final TarjetaService tarjetaService;

    public ClientePanel(PanelManager panelManager, Cliente cliente,
                        CuentaService cuentaService, TarjetaService tarjetaService) {
        super(new BorderLayout(0, 0));
        this.panelManager = panelManager;
        this.cliente = cliente;
        this.cuentaService = cuentaService;
        this.tarjetaService = tarjetaService;
        armarPanel();
    }

    private void armarPanel() {
        this.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        this.add(buildEncabezado(), BorderLayout.NORTH);
        this.add(buildCuerpo(), BorderLayout.CENTER);
        this.add(buildPie(), BorderLayout.SOUTH);
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
        JPanel panel = new JPanel(new GridLayout(1, 2, 16, 0));

        panel.add(buildSeccionCuentas());
        panel.add(buildSeccionTarjetas());

        return panel;
    }

    private JPanel buildSeccionCuentas() {
        return buildSeccion("Mis cuentas", buildListaCuentas());
    }

    private JPanel buildSeccionTarjetas() {
        return buildSeccion("Mis tarjetas", buildListaTarjetas());
    }

    private JPanel buildSeccion(String titulo, JScrollPane contenido) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        JLabel label = new JLabel(titulo);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 13f));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panel.add(label, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane buildListaCuentas() {
        List<Cuenta> cuentas = cargarCuentas();
        ListaCuentasPanel lista = new ListaCuentasPanel(cuentas, panelManager, cliente);
        return scrollSin(lista);
    }

    private JScrollPane buildListaTarjetas() {
        List<Tarjeta> tarjetas = cargarTarjetas();
        ListaTarjetasPanel lista = new ListaTarjetasPanel(tarjetas, panelManager, cliente);
        return scrollSin(lista);
    }

    private JScrollPane scrollSin(JPanel contenido) {
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
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

    private List<Tarjeta> cargarTarjetas() {
        try {
            return tarjetaService.listarTarjetasDeCliente(String.valueOf(cliente.getDni()));
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar las tarjetas: " + e.getMessage(),
                    "Error del sistema", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
}
