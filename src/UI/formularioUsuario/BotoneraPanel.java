package UI.formularioUsuario;

import javax.swing.*;
import java.awt.*;

public class BotoneraPanel extends JPanel {

    private JButton botonConfirmar;
    private JButton botonCancelar;

    public BotoneraPanel(String labelConfirmar) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 16));

        botonConfirmar = new JButton(labelConfirmar);
        botonCancelar  = new JButton("Cancelar");

        this.add(botonConfirmar);
        this.add(botonCancelar);
    }

    public JButton getBotonConfirmar() {
        return botonConfirmar;
    }

    public JButton getBotonCancelar() {
        return botonCancelar;
    }
}
