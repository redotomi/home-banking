package UI.formularioUsuario;

import UI.formularioUsuario.tablasCliente.CuentasClientePanel;
import UI.formularioUsuario.tablasCliente.TarjetasClientePanel;
import entidades.Usuario;
import service.CuentaService;
import service.TarjetaService;

import javax.swing.*;
import java.awt.*;

public class EditarClienteCompositePanel extends CamposPanel {

    private final CamposUsuarioPanel   camposUsuarioPanel;
    private final CuentasClientePanel  cuentasClientePanel;
    private final TarjetasClientePanel tarjetasClientePanel;

    public EditarClienteCompositePanel(Usuario cliente, CuentaService cuentaService, TarjetaService tarjetaService) {
        this.camposUsuarioPanel   = new CamposUsuarioPanel(cliente);
        this.cuentasClientePanel  = new CuentasClientePanel(cliente, cuentaService);
        this.tarjetasClientePanel = new TarjetasClientePanel(cliente, tarjetaService);
        armarFormulario();
    }

    @Override
    public void armarFormulario() {
        this.setLayout(new BorderLayout(0, 16));

        JPanel wrapperCampos = new JPanel(new BorderLayout());
        wrapperCampos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del usuario"));
        wrapperCampos.add(camposUsuarioPanel, BorderLayout.CENTER);

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 8, 0));
        panelTablas.add(cuentasClientePanel);
        panelTablas.add(tarjetasClientePanel);

        this.add(wrapperCampos, BorderLayout.NORTH);
        this.add(panelTablas, BorderLayout.CENTER);
    }

    public CamposUsuarioPanel getCamposUsuarioPanel() {
        return camposUsuarioPanel;
    }
}
