package UI.cliente.movimientos;

import UI.PanelManager;
import UI.cliente.ListaOperacionesPanel;
import entidades.Cliente;
import entidades.MovimientoTarjeta;
import entidades.Tarjeta;

import java.util.List;

public class ListaMovimientosPanel extends ListaOperacionesPanel {

    private final Tarjeta tarjeta;
    private final List<MovimientoTarjeta> movimientos;

    public ListaMovimientosPanel(PanelManager panelManager, Cliente cliente,
                                 Tarjeta tarjeta, List<MovimientoTarjeta> movimientos) {
        super(panelManager, cliente);
        this.tarjeta = tarjeta;
        this.movimientos = movimientos;
        armarPanel();
    }

    @Override
    protected String getTitulo() {
        return "Movimientos de tarjeta " + tarjeta.getProveedor() + " " + tarjeta.getNumero();
    }

    @Override
    protected String[] getColumnas() {
        return new String[]{"Monto", "Fecha", "Referencia"};
    }

    @Override
    protected Object[][] getFilas() {
        Object[][] filas = new Object[movimientos.size()][3];
        for (int i = 0; i < movimientos.size(); i++) {
            MovimientoTarjeta m = movimientos.get(i);
            filas[i][0] = "$ " + m.getMonto();
            filas[i][1] = m.getFecha();
            filas[i][2] = m.getReferencia();
        }
        return filas;
    }
}
