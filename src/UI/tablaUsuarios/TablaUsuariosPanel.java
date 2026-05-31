package UI.tablaUsuarios;

import UI.PanelManager;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

public class TablaUsuariosPanel extends JPanel implements ActionListener, Printable {
    private JTable tablaUsuarios;
    private TablaUsuariosModel model;
    private PanelManager panelManager;

    private JScrollPane scrollPaneParaTabla;
    private JButton botonLlenar;
    private JButton botonAgregar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonImprimir;

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

        botonImprimir = new JButton("Imprimir Tabla");
        botonImprimir.addActionListener(this);
        panelBotones.add(botonImprimir);

        this.add(panelBotones, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonImprimir) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(this);
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    /* The job did not successfully complete */
                }
            }

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
            } catch (ServiceException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        System.out.println("imprimo");
        return 0;
    }
}
