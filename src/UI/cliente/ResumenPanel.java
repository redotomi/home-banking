package UI.cliente;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

/**
 * Panel base reutilizable que muestra un cuadro con título, campos clave-valor
 * y un botón de acción. Las subclases deben llamar a armarPanel() al final
 * de su propio constructor, una vez que sus campos estén inicializados.
 */
public abstract class ResumenPanel extends JPanel {

    /** Las subclases deben invocar este método al final de su constructor. */
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

    /** Título del borde del panel. */
    protected abstract String getTitulo();

    /**
     * Campos a mostrar en orden (etiqueta → valor).
     * Se usa LinkedHashMap para preservar el orden de inserción.
     */
    protected abstract LinkedHashMap<String, String> getCampos();

    /** Texto del botón de acción principal. */
    protected abstract String getLabelBoton();

    /** Acción al presionar el botón principal. */
    protected abstract void onBotonClick();

    private JLabel crearLabelValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
