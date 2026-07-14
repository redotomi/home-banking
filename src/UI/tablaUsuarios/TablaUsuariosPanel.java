package UI.tablaUsuarios;

import UI.PanelManager;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TablaUsuariosPanel extends JPanel implements ActionListener {
    private JTable tablaUsuarios;
    private TablaUsuariosModel model;
    private PanelManager panelManager;

    private JScrollPane scrollPaneParaTabla;
    private JButton botonLlenar;
    private JButton botonAgregar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonCerrarSesion;

    private UsuarioService usuarioService;

    public TablaUsuariosPanel(PanelManager panelManager, UsuarioService usuarioService) {
        super();
        this.panelManager = panelManager;
        this.usuarioService = usuarioService;
        armarPanel();
    }

    private void armarPanel() {
        this.setLayout(new BorderLayout());

        model = new TablaUsuariosModel();
        tablaUsuarios = new JTable(model);
        scrollPaneParaTabla = new JScrollPane(tablaUsuarios);
        this.add(scrollPaneParaTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));

        botonLlenar = new JButton("Cargar Tabla");
        botonLlenar.addActionListener(this);
        panelBotones.add(botonLlenar);

        botonAgregar = new JButton("Agregar usuario");
        botonAgregar.addActionListener(this);
        panelBotones.add(botonAgregar);

        botonEditar = new JButton("Editar usuario");
        botonEditar.addActionListener(this);
        panelBotones.add(botonEditar);

        botonEliminar = new JButton("Eliminar fila");
        botonEliminar.addActionListener(this);
        panelBotones.add(botonEliminar);

        botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.addActionListener(this);
        panelBotones.add(botonCerrarSesion);

        this.add(panelBotones, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonCerrarSesion) {
            panelManager.mostrarLoginPanel();

        } else if (e.getSource() == botonEliminar) {
            int filaSeleccionada = tablaUsuarios.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccioná una fila para eliminar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int dni = (int) model.getValueAt(filaSeleccionada, TablaUsuariosModel.COLUMNA_DNI);
                    usuarioService.eliminarUsuario(dni);
                    model.getFilas().remove(filaSeleccionada);
                    model.fireTableDataChanged();
                } catch (ServiceException ex) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar el usuario: " + ex.getMessage(),
                            "Error del sistema", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }

        } else if (e.getSource() == botonAgregar) {
            panelManager.mostrarFormularioUsuario(null);

        } else if (e.getSource() == botonEditar) {
            int filaSeleccionada = tablaUsuarios.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccioná una fila para editar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            } else {
                Usuario usuario = model.getFilas().get(filaSeleccionada);
                panelManager.mostrarFormularioUsuario(usuario);
            }

        } else {
            try {
                List<Usuario> listaUsuarios = usuarioService.consultarUsuarios();
                model.setFilas(listaUsuarios);
                model.fireTableDataChanged();
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cargar la lista de usuarios: " + ex.getMessage(),
                        "Error del sistema", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
