package UI;

import UI.tablaUsuarios.TablaUsuariosPanel;
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
        frame.setBounds(100, 100, 500, 1000);
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
}
