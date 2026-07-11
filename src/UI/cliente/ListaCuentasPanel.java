package UI.cliente;

import entidades.Cuenta;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListaCuentasPanel extends JPanel {

    public ListaCuentasPanel(List<Cuenta> cuentas) {
        armarPanel(cuentas);
    }

    private void armarPanel(List<Cuenta> cuentas) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        if (cuentas == null || cuentas.isEmpty()) {
            JLabel sinCuentas = new JLabel("No tenés cuentas asociadas.", SwingConstants.CENTER);
            sinCuentas.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.add(Box.createVerticalGlue());
            this.add(sinCuentas);
            this.add(Box.createVerticalGlue());
            return;
        }

        for (Cuenta cuenta : cuentas) {
            ResumenCuentaPanel card = new ResumenCuentaPanel(cuenta);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
            this.add(card);
            this.add(Box.createVerticalStrut(10));
        }
    }
}
