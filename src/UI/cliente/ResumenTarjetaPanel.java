package UI.cliente;

import UI.PanelManager;
import entidades.Cliente;
import entidades.Tarjeta;

import java.util.LinkedHashMap;

public class ResumenTarjetaPanel extends ResumenPanel {

    private final Tarjeta tarjeta;
    private final PanelManager panelManager;
    private final Cliente cliente;

    public ResumenTarjetaPanel(Tarjeta tarjeta, PanelManager panelManager, Cliente cliente) {
        this.tarjeta = tarjeta;
        this.panelManager = panelManager;
        this.cliente = cliente;
        armarPanel();
    }

    @Override
    protected String getTitulo() {
        return tarjeta.getProveedor() + " **** " + ultimosCuatro();
    }

    @Override
    protected LinkedHashMap<String, String> getCampos() {
        LinkedHashMap<String, String> campos = new LinkedHashMap<>();
        campos.put("Número:", tarjeta.getNumero());
        campos.put("Vencimiento:", tarjeta.getVencimiento().toString());
        campos.put("Límite:", "$ " + tarjeta.getLimite());
        campos.put("Consumido:", "$ " + tarjeta.getConsumo());
        return campos;
    }

    @Override
    protected String getLabelBoton() {
        return "Movimientos";
    }

    @Override
    protected void onBotonClick() {
        panelManager.mostrarMovimientosDeTarjeta(tarjeta, cliente);
    }

    private String ultimosCuatro() {
        String num = tarjeta.getNumero();
        return num.length() >= 4 ? num.substring(num.length() - 4) : num;
    }
}
