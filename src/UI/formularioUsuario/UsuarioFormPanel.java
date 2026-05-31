package UI.formularioUsuario;

import UI.PanelManager;
import entidades.Administrador;
import entidades.Cliente;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioFormPanel extends JPanel implements ActionListener {

    private PanelManager panelManager;
    private UsuarioService usuarioService;

    private Usuario usuarioAEditar;

    private JTextField campoDni;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JComboBox<String> comboRol;  // solo visible en creación

    private JButton botonConfirmar;
    private JButton botonCancelar;

    public UsuarioFormPanel(PanelManager panelManager, UsuarioService usuarioService, Usuario usuarioAEditar) {
        super();
        this.panelManager = panelManager;
        this.usuarioService = usuarioService;
        this.usuarioAEditar = usuarioAEditar;
        armarPanel();
    }

    private void armarPanel() {
        boolean modoCreacion = (usuarioAEditar == null);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JPanel camposPanel = new JPanel(new GridLayout(0, 2, 8, 12));

        camposPanel.add(new JLabel("DNI:"));
        campoDni = new JTextField();
        if (!modoCreacion) campoDni.setText(String.valueOf(usuarioAEditar.getDni()));
        camposPanel.add(campoDni);

        camposPanel.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        if (!modoCreacion) campoNombre.setText(usuarioAEditar.getNombre());
        camposPanel.add(campoNombre);

        camposPanel.add(new JLabel("Apellido:"));
        campoApellido = new JTextField();
        if (!modoCreacion) campoApellido.setText(usuarioAEditar.getApellido());
        camposPanel.add(campoApellido);

        if (modoCreacion) {
            camposPanel.add(new JLabel("Rol:"));
            comboRol = new JComboBox<>(new String[]{"Cliente", "Administrador"});
            camposPanel.add(comboRol);
        }

        this.add(camposPanel, BorderLayout.CENTER);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 16));
        botonConfirmar = new JButton(modoCreacion ? "Crear usuario" : "Guardar cambios");
        botonConfirmar.addActionListener(this);
        botonesPanel.add(botonConfirmar);

        botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(this);
        botonesPanel.add(botonCancelar);

        this.add(botonesPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonCancelar) {
            panelManager.mostrarTablaUsuariosPanel();
            return;
        }

        if (e.getSource() == botonConfirmar) {
            String dniTexto = campoDni.getText().trim();
            String nombre = campoNombre.getText().trim();
            String apellido = campoApellido.getText().trim();

            if (dniTexto.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completá todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int dni;
            try {
                dni = Integer.parseInt(dniTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El DNI debe ser un número.", "DNI inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }


            try {
                Usuario existente = usuarioService.buscarUsuario(dni);
                boolean esDuplicado = existente != null;

                if (esDuplicado && (usuarioAEditar == null || existente.getId() != usuarioAEditar.getId())) {
                    JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese DNI.", "DNI duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (ServiceException ex) {
                // REVISAR
            }

            try {
                if (usuarioAEditar == null) {
                    String rolElegido = (String) comboRol.getSelectedItem();
                    Usuario nuevoUsuario;
                    if ("Administrador".equals(rolElegido)) {
                        nuevoUsuario = new Administrador(0, nombre, apellido, dni);
                    } else {
                        nuevoUsuario = new Cliente(0, nombre, apellido, dni);
                    }
                    usuarioService.agregarUsuario(nuevoUsuario);
                } else {
                    usuarioAEditar.setDni(dni);
                    usuarioAEditar.setNombre(nombre);
                    usuarioAEditar.setApellido(apellido);
                    usuarioService.actualizarUsuario(usuarioAEditar);
                }
                panelManager.mostrarTablaUsuariosPanel();
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
