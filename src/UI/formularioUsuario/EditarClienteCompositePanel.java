package UI.formularioUsuario;

import entidades.Usuario;
import service.CuentaService;

import javax.swing.*;
import java.awt.*;

public class EditarClienteCompositePanel extends CamposPanel {

    private final CamposUsuarioPanel camposUsuarioPanel;
    private final CuentasClientePanel cuentasClientePanel;

    public EditarClienteCompositePanel(Usuario cliente, CuentaService cuentaService) {
        this.camposUsuarioPanel  = new CamposUsuarioPanel(cliente);
        this.cuentasClientePanel = new CuentasClientePanel(cliente, cuentaService);
        armarFormulario();
    }

    @Override
    public void armarFormulario() {
        this.setLayout(new BorderLayout(0, 16));

        JPanel wrapperCampos = new JPanel(new BorderLayout());
        wrapperCampos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del usuario"));
        wrapperCampos.add(camposUsuarioPanel, BorderLayout.CENTER);

        this.add(wrapperCampos, BorderLayout.NORTH);
        this.add(cuentasClientePanel, BorderLayout.CENTER);
    }

    public CamposUsuarioPanel getCamposUsuarioPanel() {
        return camposUsuarioPanel;
    }
}
