package UI.formularioUsuario;

import UI.PanelManager;
import entidades.Administrador;
import entidades.Cliente;
import entidades.Usuario;
import exceptions.UIExceptions.EntradaInvalidaException;
import exceptions.serviceExceptions.DniDuplicadoException;
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
    private boolean modoRegistro;  // true cuando viene del login para registrarse

    private JTextField campoDni;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JComboBox<String> comboRol;  // solo visible en creación

    private JButton botonConfirmar;
    private JButton botonCancelar;

    public UsuarioFormPanel(PanelManager panelManager, UsuarioService usuarioService, Usuario usuarioAEditar) {
        this(panelManager, usuarioService, usuarioAEditar, false);
    }

    public UsuarioFormPanel(PanelManager panelManager, UsuarioService usuarioService, Usuario usuarioAEditar, boolean modoRegistro) {
        super();
        this.panelManager = panelManager;
        this.usuarioService = usuarioService;
        this.usuarioAEditar = usuarioAEditar;
        this.modoRegistro = modoRegistro;
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
            if (modoRegistro) {
                panelManager.mostrarLoginPanel();
            } else {
                panelManager.mostrarTablaUsuariosPanel();
            }
            return;
        }

        if (e.getSource() == botonConfirmar) {
            try {
                // ── Validaciones de formato (responsabilidad de la UI) ────────
                int dni = parsearCampos();

                String nombre = campoNombre.getText().trim();
                String apellido = campoApellido.getText().trim();

                // ── Llamada al servicio (valida reglas de negocio internamente) ─
                if (usuarioAEditar == null) {
                    String rolElegido = (String) comboRol.getSelectedItem();
                    Usuario nuevoUsuario = "Administrador".equals(rolElegido)
                            ? new Administrador(0, nombre, apellido, dni)
                            : new Cliente(0, nombre, apellido, dni);

                    usuarioService.agregarUsuario(nuevoUsuario);

                    if (modoRegistro && nuevoUsuario instanceof Cliente) {
                        JOptionPane.showMessageDialog(this, "Cuenta creada correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                        panelManager.mostrarLoginPanel();
                    } else {
                        panelManager.mostrarTablaUsuariosPanel();
                    }
                } else {
                    usuarioAEditar.setDni(dni);
                    usuarioAEditar.setNombre(nombre);
                    usuarioAEditar.setApellido(apellido);
                    usuarioService.actualizarUsuario(usuarioAEditar);
                    panelManager.mostrarTablaUsuariosPanel();
                }

            } catch (EntradaInvalidaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            } catch (DniDuplicadoException ex) {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese DNI.", "DNI duplicado", JOptionPane.WARNING_MESSAGE);
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el usuario: " + ex.getMessage(), "Error del sistema", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }


    private int parsearCampos() throws EntradaInvalidaException {
        String dniTexto = campoDni.getText().trim();
        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();

        if (dniTexto.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            throw new EntradaInvalidaException("Completá todos los campos.");
        }
        try {
            return Integer.parseInt(dniTexto);
        } catch (NumberFormatException ex) {
            throw new EntradaInvalidaException("El DNI debe ser un número.");
        }
    }
}
