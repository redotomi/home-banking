package UI.cliente.transferencias;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Cuenta;
import exceptions.serviceExceptions.CuentaNoEncontradaException;
import exceptions.serviceExceptions.ServiceException;
import exceptions.UIExceptions.EntradaInvalidaException;
import service.CuentaService;
import service.TransferenciaService;

import javax.swing.*;
import java.awt.*;

public class TransferenciaFormPanel extends JPanel {

    private final Cuenta cuentaOrigen;
    private final Cliente cliente;
    private final CuentaService cuentaService;
    private final TransferenciaService transferenciaService;
    private final PanelManager panelManager;

    private JTextField campoBusqueda;
    private JLabel labelInfoDestino;
    private JTextField campoMonto;

    private Cuenta cuentaDestinoEncontrada;

    public TransferenciaFormPanel(Cuenta cuentaOrigen, Cliente cliente, CuentaService cuentaService,
                                  TransferenciaService transferenciaService, PanelManager panelManager) {
        this.cuentaOrigen = cuentaOrigen;
        this.cliente = cliente;
        this.cuentaService = cuentaService;
        this.transferenciaService = transferenciaService;
        this.panelManager = panelManager;
        armarPanel();
    }

    private void armarPanel() {
        this.setLayout(new BorderLayout(0, 12));
        this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        this.add(buildSeccionOrigen(), BorderLayout.NORTH);
        this.add(buildSeccionDestino(), BorderLayout.CENTER);
        this.add(buildBotonera(), BorderLayout.SOUTH);
    }

    private JPanel buildSeccionOrigen() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Cuenta origen"));

        panel.add(new JLabel("Cuenta:"));
        panel.add(new JLabel(cuentaOrigen.getNombreCuenta()));
        panel.add(new JLabel("CBU:"));
        panel.add(new JLabel(cuentaOrigen.getCBU()));
        panel.add(new JLabel("Saldo disponible:"));
        panel.add(new JLabel(cuentaOrigen.getMonto() + " " + cuentaOrigen.getMoneda()));

        return panel;
    }

    private JPanel buildSeccionDestino() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));

        JPanel busqueda = new JPanel(new BorderLayout(6, 0));
        busqueda.setBorder(BorderFactory.createTitledBorder("Cuenta destino"));

        campoBusqueda = new JTextField();
        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(e -> buscarCuentaDestino());

        busqueda.add(new JLabel("CBU o alias:"), BorderLayout.WEST);
        busqueda.add(campoBusqueda, BorderLayout.CENTER);
        busqueda.add(botonBuscar, BorderLayout.EAST);

        labelInfoDestino = new JLabel(" ");
        labelInfoDestino.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 0));

        panel.add(busqueda, BorderLayout.NORTH);
        panel.add(labelInfoDestino, BorderLayout.CENTER);

        JPanel montoPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        montoPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        montoPanel.add(new JLabel("Monto a transferir:"));
        campoMonto = new JTextField();
        montoPanel.add(campoMonto);
        panel.add(montoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildBotonera() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> panelManager.mostrarPantallaCliente(cliente));

        JButton botonConfirmar = new JButton("Confirmar transferencia");
        botonConfirmar.addActionListener(e -> confirmarTransferencia());

        panel.add(botonCancelar);
        panel.add(botonConfirmar);

        return panel;
    }

    private void buscarCuentaDestino() {
        String valor = campoBusqueda.getText().trim();
        if (valor.isEmpty()) {
            labelInfoDestino.setText("Ingresá un CBU o alias.");
            cuentaDestinoEncontrada = null;
            return;
        }

        try {
            Cuenta encontrada = cuentaService.buscarCuentaPorAliasOCbu(valor);
            if (encontrada.getCBU().equals(cuentaOrigen.getCBU())) {
                labelInfoDestino.setText("No podés transferir a la misma cuenta.");
                cuentaDestinoEncontrada = null;
                return;
            }
            cuentaDestinoEncontrada = encontrada;
            labelInfoDestino.setText("Encontrada: " + encontrada.getNombreCuenta()
                    + "  |  Alias: " + encontrada.getAlias()
                    + "  |  Moneda: " + encontrada.getMoneda());
        } catch (CuentaNoEncontradaException e) {
            labelInfoDestino.setText("No se encontró ninguna cuenta con ese CBU o alias.");
            cuentaDestinoEncontrada = null;
        } catch (ServiceException e) {
            labelInfoDestino.setText("Error al buscar la cuenta.");
            cuentaDestinoEncontrada = null;
        }
    }

    private void confirmarTransferencia() {
        if (cuentaDestinoEncontrada == null) {
            JOptionPane.showMessageDialog(this, "Buscá y seleccioná una cuenta destino primero.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int monto;
        try {
            monto = Integer.parseInt(campoMonto.getText().trim());
            if (monto <= 0) throw new EntradaInvalidaException("El monto debe ser mayor a 0.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número entero válido.",
                    "Monto inválido", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (EntradaInvalidaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Monto inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            transferenciaService.realizarTransferencia(cuentaOrigen.getCBU(), cuentaDestinoEncontrada.getCBU(), monto);
            JOptionPane.showMessageDialog(this, "Transferencia realizada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            panelManager.mostrarPantallaCliente(cliente);
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error en la transferencia", JOptionPane.ERROR_MESSAGE);
        }
    }
}
