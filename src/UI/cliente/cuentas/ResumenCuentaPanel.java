package UI.cliente.cuentas;

import UI.PanelManager;
import UI.cliente.ResumenPanel;
import entidades.Cliente;
import entidades.Cuenta;

import java.util.LinkedHashMap;

public class ResumenCuentaPanel extends ResumenPanel {

    private final Cuenta cuenta;
    private final PanelManager panelManager;
    private final Cliente cliente;

    public ResumenCuentaPanel(Cuenta cuenta, PanelManager panelManager, Cliente cliente) {
        this.cuenta = cuenta;
        this.panelManager = panelManager;
        this.cliente = cliente;
        armarPanel();
    }

    @Override
    protected String getTitulo() {
        return cuenta.getNombreCuenta();
    }

    @Override
    protected LinkedHashMap<String, String> getCampos() {
        LinkedHashMap<String, String> campos = new LinkedHashMap<>();
        campos.put("CBU:", cuenta.getCBU());
        campos.put("Alias:", cuenta.getAlias());
        campos.put("Moneda:", cuenta.getMoneda());
        campos.put("Saldo:", cuenta.getMonto() + " " + cuenta.getMoneda());
        return campos;
    }

    @Override
    protected String getLabelBoton() {
        return "Transferencias";
    }

    @Override
    protected void onBotonClick() {
        panelManager.mostrarTransferenciasDeCliente(cuenta, cliente);
    }
}
