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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TablaUsuariosPanel extends JPanel implements ActionListener, Printable {
    private JTable tablaUsuarios;
    private TablaUsuariosModel model;
    private PanelManager panelManager;

    private JScrollPane scrollPaneParaTabla;
    private JButton botonLlenar;
    private JButton botonAgregar;
    private JButton botonImprimir;

    private UsuarioService usuarioService;

    public TablaUsuariosPanel(PanelManager panelManager, UsuarioService usuarioService) {
        super();
        this.panelManager = panelManager;
        this.usuarioService = usuarioService;
        armarPanel();
    }

    private void armarPanel() {
        this.setLayout(new FlowLayout());

        model = new TablaUsuariosModel();
        tablaUsuarios = new JTable(model);
        scrollPaneParaTabla = new JScrollPane(tablaUsuarios);
        this.add(scrollPaneParaTabla);

        //ESCUCHO EVENTOS TABLA --- si le das enter cuando editas, entra aca
        TableCellListener tcl = new TableCellListener(tablaUsuarios, new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                TableCellListener tcl = (TableCellListener)e.getSource();
                System.out.println("Row   : " + tcl.getRow());
                System.out.println("Column: " + tcl.getColumn());
                System.out.println("Old   : " + tcl.getOldValue());
                System.out.println("New   : " + tcl.getNewValue());

            }
        });

        botonLlenar = new JButton("Cargar Tabla");
        botonLlenar.addActionListener(this);
        this.add(botonLlenar);

        botonAgregar = new JButton("Cargar fila");
        botonAgregar.addActionListener(this);
        this.add(botonAgregar);

        botonImprimir = new JButton("Imprimir Tabla");
        botonImprimir.addActionListener(this);
        this.add(botonImprimir);

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

        } else if (e.getSource() == botonAgregar) {
            Random r = new Random();
            int x = r.nextInt(100);
            int dni = r.nextInt(1000) * r.nextInt(1000);

            Usuario u1 = new Usuario(dni / x, "test" + x, "mail" + x, dni);

            model.getFilas().add(u1);

            model.fireTableDataChanged();

            /*
             * tablaUsuarios.revalidate();
             *
             * this.revalidate(); this.repaint();
             */

        } else {
            try {
                List<Usuario> listaUsuarios =  usuarioService.consultarUsuarios();
                model.setFilas(listaUsuarios);

                model.fireTableDataChanged();
            } catch (ServiceException exception) {
                exception.printStackTrace();
            }
//            Usuario u1 = new Usuario(1, "pipo", "pipo@river.com", 123);
//            Usuario u2 = new Usuario(2, "coco", "coco@boca.com", 456);
//            Usuario u3 = new Usuario(3, "tolo", "tolo@rojo.com", 789);
//            Usuario u4 = new Usuario(4, "boquita", "sensini@newells.com", 100);
//            List<Usuario> lista = new ArrayList<Usuario>();
//            lista.add(u1);
//            lista.add(u2);
//            lista.add(u3);
//            lista.add(u4);

        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        System.out.println("imprimo");
        return 0;
    }
}
