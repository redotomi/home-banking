package UI.formularioUsuario;

import UI.formularioUsuario.tablasCliente.CuentasClientePanel;
import UI.formularioUsuario.tablasCliente.MovimientosClientePanel;
import UI.formularioUsuario.tablasCliente.TarjetasClientePanel;
import entidades.Usuario;
import service.CuentaService;
import service.MovimientoTarjetaService;
import service.TarjetaService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class EditarClienteCompositePanel extends CamposPanel {

    private final CamposUsuarioPanel      camposUsuarioPanel;
    private final CuentasClientePanel     cuentasClientePanel;
    private final TarjetasClientePanel    tarjetasClientePanel;
    private final MovimientosClientePanel movimientosClientePanel;

    public EditarClienteCompositePanel(Usuario cliente,
                                       CuentaService cuentaService,
                                       TarjetaService tarjetaService,
                                       MovimientoTarjetaService movimientoTarjetaService) {
        this.camposUsuarioPanel      = new CamposUsuarioPanel(cliente);
        this.cuentasClientePanel     = new CuentasClientePanel(cliente, cuentaService);
        this.tarjetasClientePanel    = new TarjetasClientePanel(cliente, tarjetaService);
        this.movimientosClientePanel = new MovimientosClientePanel(movimientoTarjetaService);

        armarFormulario();
        conectarSeleccionTarjeta();
    }

    @Override
    public void armarFormulario() {
        this.setLayout(new BorderLayout(0, 16));

        // ── Datos del usuario ──────────────────────────────────────────────
        JPanel wrapperCampos = new JPanel(new BorderLayout());
        wrapperCampos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del usuario"));
        wrapperCampos.add(camposUsuarioPanel, BorderLayout.CENTER);

        // ── Fila superior: Cuentas | Tarjetas ─────────────────────────────
        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 8, 0));
        panelTablas.add(cuentasClientePanel);
        panelTablas.add(tarjetasClientePanel);

        // ── Zona central: tablas arriba + movimientos abajo ───────────────
        JPanel panelCentral = new JPanel(new BorderLayout(0, 8));
        panelCentral.add(panelTablas, BorderLayout.CENTER);
        panelCentral.add(movimientosClientePanel, BorderLayout.SOUTH);

        this.add(wrapperCampos, BorderLayout.NORTH);
        this.add(panelCentral, BorderLayout.CENTER);
    }

    public CamposUsuarioPanel getCamposUsuarioPanel() {
        return camposUsuarioPanel;
    }

    // ── Conexión de eventos ────────────────────────────────────────────────

    /**
     * Agrega un {@link ListSelectionListener} a la tabla de tarjetas para que,
     * al seleccionar una fila, el panel de movimientos se actualice automáticamente.
     * Si se deselecciona (valueIsAdjusting == false y sin selección), limpia el panel.
     */
    private void conectarSeleccionTarjeta() {
        tarjetasClientePanel.getTabla().getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting()) return;
                        String numero = tarjetasClientePanel.getNumeroTarjetaSeleccionada();
                        if (numero != null) {
                            movimientosClientePanel.cargarTarjeta(numero);
                        } else {
                            movimientosClientePanel.limpiar();
                        }
                    }
                });
    }
}
