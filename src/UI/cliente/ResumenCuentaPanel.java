package UI.cliente;

import entidades.Cuenta;

import javax.swing.*;
import java.awt.*;

public class ResumenCuentaPanel extends JPanel {

    private final Cuenta cuenta;

    public ResumenCuentaPanel(Cuenta cuenta) {
        this.cuenta = cuenta;
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
    }

    private JLabel crearLabelValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
