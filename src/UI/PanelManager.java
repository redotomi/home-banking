package UI;

import UI.formularioUsuario.UsuarioFormPanel;
import UI.login.LoginPanel;
import UI.tablaUsuarios.TablaUsuariosPanel;
import entidades.Usuario;
import service.UsuarioService;

import javax.swing.*;

public class PanelManager {
    private JFrame frame;
    private UsuarioService usuarioService;

    private TablaUsuariosPanel tablaUsuariosPanel;

    public PanelManager(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void armarManager() {
        frame = new JFrame();
        frame.setBounds(100, 100, 960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tablaUsuariosPanel = new TablaUsuariosPanel(this, usuarioService);
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    public void mostrarTablaUsuariosPanel() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(tablaUsuariosPanel);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarFormularioUsuario(Usuario usuario) {
        frame.getContentPane().removeAll();
        UsuarioFormPanel formulario = new UsuarioFormPanel(this, usuarioService, usuario);
        frame.getContentPane().add(formulario);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarFormularioRegistro() {
        frame.getContentPane().removeAll();
        UsuarioFormPanel formulario = new UsuarioFormPanel(this, usuarioService, null, true);
        frame.getContentPane().add(formulario);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarLoginPanel() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new LoginPanel(this, usuarioService));
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }
}
