package UI.formularioUsuario;

import entidades.Usuario;

import javax.swing.*;
import java.awt.*;

public class CamposUsuarioPanel extends CamposPanel {

    private final Usuario usuarioAEditar;

    private JTextField campoDni;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JComboBox<String> comboRol;

    public CamposUsuarioPanel(Usuario usuarioAEditar) {
        this.usuarioAEditar = usuarioAEditar;
        armarFormulario();
    }

    @Override
    public void armarFormulario() {
        boolean modoCreacion = (usuarioAEditar == null);

        this.setLayout(new GridLayout(0, 2, 8, 12));

        this.add(new JLabel("DNI:"));
        campoDni = new JTextField();
        if (!modoCreacion) campoDni.setText(String.valueOf(usuarioAEditar.getDni()));
        this.add(campoDni);

        this.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        if (!modoCreacion) campoNombre.setText(usuarioAEditar.getNombre());
        this.add(campoNombre);

        this.add(new JLabel("Apellido:"));
        campoApellido = new JTextField();
        if (!modoCreacion) campoApellido.setText(usuarioAEditar.getApellido());
        this.add(campoApellido);

        if (modoCreacion) {
            this.add(new JLabel("Rol:"));
            comboRol = new JComboBox<>(new String[]{"Cliente", "Administrador"});
            this.add(comboRol);
        }
    }

    public JTextField getCampoDni() {
        return campoDni;
    }

    public JTextField getCampoNombre() {
        return campoNombre;
    }

    public JTextField getCampoApellido() {
        return campoApellido;
    }

    public JComboBox<String> getComboRol() {
        return comboRol;
    }
}
