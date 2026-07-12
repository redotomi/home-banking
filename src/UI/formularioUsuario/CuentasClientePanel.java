package UI.formularioUsuario;

import entidades.Cuenta;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.CuentaService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CuentasClientePanel extends CamposPanel {

    private static final String[] TIPOS_CUENTA = {
        "Caja Ahorro Pesos",
        "Caja Ahorro Dólares",
        "Cuenta Corriente Pesos",
        "Cuenta Corriente Dólares"
    };

    private static final String[] COLUMNAS = { "Tipo de cuenta", "Moneda", "Saldo" };

    private final Usuario cliente;
    private final CuentaService cuentaService;

    private DefaultTableModel tableModel;
    private JTable tablaCuentas;
    private JComboBox<String> comboTipoCuenta;

    public CuentasClientePanel(Usuario cliente, CuentaService cuentaService) {
        this.cliente = cliente;
        this.cuentaService = cuentaService;
        armarFormulario();
    }

    @Override
    public void armarFormulario() {
        this.setLayout(new BorderLayout(0, 8));
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Cuentas",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        tableModel = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCuentas = new JTable(tableModel);
        tablaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCuentas.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(tablaCuentas);
        scroll.setPreferredSize(new Dimension(0, 130));
        this.add(scroll, BorderLayout.CENTER);

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        comboTipoCuenta = new JComboBox<>(TIPOS_CUENTA);
        JButton botonAgregar = new JButton("Agregar cuenta");

        botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarCuenta();
            }
        });

        panelAgregar.add(new JLabel("Tipo:"));
        panelAgregar.add(comboTipoCuenta);
        panelAgregar.add(botonAgregar);
        this.add(panelAgregar, BorderLayout.SOUTH);

        cargarCuentas();
    }

    private void cargarCuentas() {
        tableModel.setRowCount(0);
        try {
            List<Cuenta> cuentas = cuentaService.listarCuentasDeCliente(cliente.getDni());
            for (Cuenta c : cuentas) {
                tableModel.addRow(new Object[]{ c.getNombreCuenta(), c.getMoneda(), c.getMonto() });
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar las cuentas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarCuenta() {
        String tipoCuenta = (String) comboTipoCuenta.getSelectedItem();
        try {
            cuentaService.crearCuenta(cliente, tipoCuenta);
            cargarCuentas();
            JOptionPane.showMessageDialog(this,
                    "Cuenta \"" + tipoCuenta + "\" agregada correctamente.",
                    "Cuenta creada", JOptionPane.INFORMATION_MESSAGE);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear la cuenta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
