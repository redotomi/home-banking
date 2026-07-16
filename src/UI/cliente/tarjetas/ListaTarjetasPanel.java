package UI.cliente.tarjetas;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Tarjeta;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListaTarjetasPanel extends JPanel {

    public ListaTarjetasPanel(List<Tarjeta> tarjetas, PanelManager panelManager, Cliente cliente) {
        armarPanel(tarjetas, panelManager, cliente);
    }

    private void armarPanel(List<Tarjeta> tarjetas, PanelManager panelManager, Cliente cliente) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        if (tarjetas == null || tarjetas.isEmpty()) {
            JLabel sinTarjetas = new JLabel("No tenés tarjetas asociadas.", SwingConstants.CENTER);
            sinTarjetas.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.add(Box.createVerticalGlue());
            this.add(sinTarjetas);
            this.add(Box.createVerticalGlue());
            return;
        }

        for (Tarjeta tarjeta : tarjetas) {
            ResumenTarjetaPanel card = new ResumenTarjetaPanel(tarjeta, panelManager, cliente);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
            this.add(card);
            this.add(Box.createVerticalStrut(10));
        }
    }
}
