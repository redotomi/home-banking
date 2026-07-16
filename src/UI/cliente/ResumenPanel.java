package UI.cliente;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;


public abstract class ResumenPanel extends JPanel {

    protected void armarPanel() {
        this.setLayout(new BorderLayout(0, 6));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(getTitulo()),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));

        JPanel campos = new JPanel(new GridLayout(0, 2, 8, 4));
        getCampos().forEach((etiqueta, valor) -> {
            campos.add(new JLabel(etiqueta));
            campos.add(crearLabelValor(valor));
        });
        this.add(campos, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        JButton boton = new JButton(getLabelBoton());
        boton.addActionListener(e -> onBotonClick());
        acciones.add(boton);
        this.add(acciones, BorderLayout.SOUTH);
    }

    protected abstract String getTitulo();

    protected abstract LinkedHashMap<String, String> getCampos();

    protected abstract String getLabelBoton();

    protected abstract void onBotonClick();

    private JLabel crearLabelValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
