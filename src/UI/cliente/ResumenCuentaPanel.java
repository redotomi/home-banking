package UI.cliente;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Cuenta;
import service.CuentaService;
import service.TransferenciaService;

import javax.swing.*;
import java.awt.*;

public class ResumenCuentaPanel extends JPanel {

    private final Cuenta cuenta;
    private final PanelManager panelManager;
    private final Cliente cliente;
    private final CuentaService cuentaService;
    private final TransferenciaService transferenciaService;

    public ResumenCuentaPanel(Cuenta cuenta, PanelManager panelManager, Cliente cliente,
                              CuentaService cuentaService, TransferenciaService transferenciaService) {
        this.cuenta = cuenta;
        this.panelManager = panelManager;
        this.cliente = cliente;
        this.cuentaService = cuentaService;
        this.transferenciaService = transferenciaService;
        armarPanel();
    }

    private void armarPanel() {
        this.setLayout(new BorderLayout(0, 6));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(cuenta.getNombreCuenta()),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));

        JPanel campos = new JPanel(new GridLayout(0, 2, 8, 4));

        campos.add(new JLabel("CBU:"));
        campos.add(crearLabelValor(cuenta.getCBU()));

        campos.add(new JLabel("Alias:"));
        campos.add(crearLabelValor(cuenta.getAlias()));

        campos.add(new JLabel("Moneda:"));
        campos.add(crearLabelValor(cuenta.getMoneda()));

        campos.add(new JLabel("Saldo:"));
        campos.add(crearLabelValor(cuenta.getMonto() + " " + cuenta.getMoneda()));

        this.add(campos, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        JButton botonTransferir = new JButton("Transferir");
        botonTransferir.addActionListener(e -> panelManager.mostrarFormularioTransferencia(cuenta, cliente));
        acciones.add(botonTransferir);

        this.add(acciones, BorderLayout.SOUTH);
    }

    private JLabel crearLabelValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
