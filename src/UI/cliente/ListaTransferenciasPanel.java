package UI.cliente;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Transferencia;

import java.util.List;

public class ListaTransferenciasPanel extends ListaOperacionesPanel {

    private final List<Transferencia> transferencias;

    public ListaTransferenciasPanel(PanelManager panelManager, Cliente cliente,
                                    List<Transferencia> transferencias) {
        super(panelManager, cliente);
        this.transferencias = transferencias;
        armarPanel();
    }

    @Override
    protected String getTitulo() {
        return "Historial de transferencias";
    }

    @Override
    protected String[] getColumnas() {
        return new String[]{"Monto", "Fecha", "CBU destino"};
    }

    @Override
    protected Object[][] getFilas() {
        Object[][] filas = new Object[transferencias.size()][3];
        for (int i = 0; i < transferencias.size(); i++) {
            Transferencia t = transferencias.get(i);
            filas[i][0] = "$ " + t.getMonto() + " " + t.getMoneda();
            filas[i][1] = t.getFecha();
            filas[i][2] = t.getCbuCuentaDestino();
        }
        return filas;
    }
}
