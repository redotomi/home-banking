package UI.cliente.transferencias;

import UI.PanelManager;
import UI.cliente.ListaOperacionesPanel;
import entidades.Cliente;
import entidades.Cuenta;
import entidades.Transferencia;

import javax.swing.*;
import java.awt.*;

import java.util.List;

public class ListaTransferenciasPanel extends ListaOperacionesPanel {

    private final Cuenta cuenta;
    private final List<Transferencia> transferencias;

    public ListaTransferenciasPanel(PanelManager panelManager, Cliente cliente,
                                    Cuenta cuenta, List<Transferencia> transferencias) {
        super(panelManager, cliente);
        this.cuenta = cuenta;
        this.transferencias = transferencias;
        armarPanel();
    }

    @Override
    protected String getTitulo() {
        return "Historial de transferencias";
    }

    @Override
    protected String[] getColumnas() {
        return new String[]{"Monto", "Fecha", "Contraparte (CBU)"};
    }

    @Override
    protected Object[][] getFilas() {
        Object[][] filas = new Object[transferencias.size()][3];
        for (int i = 0; i < transferencias.size(); i++) {
            Transferencia t = transferencias.get(i);
            boolean esOrigen = t.getCbuCuentaOrigen().equals(cuenta.getCBU());
            String signo = esOrigen ? "-" : "+";
            
            filas[i][0] = signo + " $ " + t.getMonto() + " " + t.getMoneda();
            filas[i][1] = t.getFecha();
            filas[i][2] = esOrigen ? t.getCbuCuentaDestino() : t.getCbuCuentaOrigen();
        }
        return filas;
    }

    @Override
    protected JPanel buildPie() {
        JPanel panel = super.buildPie();
        JButton botonNueva = new JButton("Nueva transferencia");
        botonNueva.addActionListener(e -> panelManager.mostrarFormularioTransferencia(cuenta, cliente));
        panel.add(botonNueva, 0); 
        return panel;
    }
}
