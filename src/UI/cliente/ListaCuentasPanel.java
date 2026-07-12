package UI.cliente;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Cuenta;
import service.CuentaService;
import service.TransferenciaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListaCuentasPanel extends JPanel {

    public ListaCuentasPanel(List<Cuenta> cuentas, PanelManager panelManager, Cliente cliente,
                             CuentaService cuentaService, TransferenciaService transferenciaService) {
        armarPanel(cuentas, panelManager, cliente, cuentaService, transferenciaService);
    }

    private void armarPanel(List<Cuenta> cuentas, PanelManager panelManager, Cliente cliente,
                            CuentaService cuentaService, TransferenciaService transferenciaService) {
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
            ResumenCuentaPanel card = new ResumenCuentaPanel(cuenta, panelManager, cliente, cuentaService, transferenciaService);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
            this.add(card);
            this.add(Box.createVerticalStrut(10));
        }
    }
}
