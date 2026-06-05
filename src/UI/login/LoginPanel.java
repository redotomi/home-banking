package UI.login;

import UI.PanelManager;
import entidades.Administrador;
import entidades.Usuario;
import exceptions.UIExceptions.EntradaInvalidaException;
import exceptions.serviceExceptions.ServiceException;
import service.UsuarioService;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel implements ActionListener {

    private PanelManager panelManager;
    private UsuarioService usuarioService;

    private JButton botonIniciarSesion;
    private JButton botonRegistrarse;

    private JTextField campoLoginDni;
    private JButton botonLogin;

    private JButton botonIrAFormulario;

    private JPanel areaContenido;
    private JPanel panelLogin;
    private JPanel panelRegistro;

    public LoginPanel(PanelManager panelManager, UsuarioService usuarioService) {
        super(new BorderLayout(0, 10));
        this.panelManager = panelManager;
        this.usuarioService = usuarioService;
        armarPanel();
    }

    private void armarPanel() {

        JLabel titulo = new JLabel("LaboBank", SwingConstants.CENTER);
        this.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 10));

        JPanel togglePanel = new JPanel(new GridLayout(1, 2));
        botonIniciarSesion = new JButton("Iniciar sesión");
        botonRegistrarse   = new JButton("Registrarse");
        botonIniciarSesion.addActionListener(this);
        botonRegistrarse.addActionListener(this);
        togglePanel.add(botonIniciarSesion);
        togglePanel.add(botonRegistrarse);
        centro.add(togglePanel, BorderLayout.NORTH);

        areaContenido = new JPanel(new BorderLayout());
        panelLogin    = buildLoginPanel();
        panelRegistro = buildRegistroPanel();
        areaContenido.add(panelLogin, BorderLayout.CENTER);
        centro.add(areaContenido, BorderLayout.CENTER);

        this.add(centro, BorderLayout.CENTER);
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));

        JPanel form = new JPanel(new GridLayout(1, 2, 8, 0));
        form.add(new JLabel("DNI (Admin - 11223344):"));
        campoLoginDni = new JTextField();
        form.add(campoLoginDni);
        panel.add(form, BorderLayout.CENTER);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonLogin = new JButton("Ingresar");
        botonLogin.addActionListener(this);
        botonesPanel.add(botonLogin);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildRegistroPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));

        JLabel info = new JLabel("Completá el formulario para crear tu cuenta.", SwingConstants.CENTER);
        panel.add(info, BorderLayout.CENTER);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonIrAFormulario = new JButton("Completar formulario");
        botonIrAFormulario.addActionListener(this);
        botonesPanel.add(botonIrAFormulario);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void mostrarTab(boolean login) {
        areaContenido.removeAll();
        areaContenido.add(login ? panelLogin : panelRegistro, BorderLayout.CENTER);
        areaContenido.revalidate();
        areaContenido.repaint();
    }

    private void manejarLogin() {
        try {
            int dni = parsearDni(campoLoginDni.getText().trim());

            Usuario usuario = usuarioService.buscarUsuario(dni);

            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró ningún usuario con ese DNI.",
                        "Usuario no encontrado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuario instanceof Administrador) {
                panelManager.mostrarTablaUsuariosPanel();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido() + ".\n" +
                        "El acceso al sistema de clientes estará disponible pronto.",
                        "Inicio de sesión exitoso", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (EntradaInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Entrada inválida", JOptionPane.WARNING_MESSAGE);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar el usuario: " + ex.getMessage(),
                    "Error del sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parsearDni(String texto) throws EntradaInvalidaException {
        if (texto.isEmpty()) {
            throw new EntradaInvalidaException("Ingresá tu DNI.");
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            throw new EntradaInvalidaException("El DNI debe ser un número.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonIniciarSesion) {
            mostrarTab(true);
        } else if (e.getSource() == botonRegistrarse) {
            mostrarTab(false);
        } else if (e.getSource() == botonLogin) {
            manejarLogin();
        } else if (e.getSource() == botonIrAFormulario) {
            panelManager.mostrarFormularioRegistro();
        }
    }
}
