package UI.formularioUsuario;

import UI.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractPantallaAltaPanel extends JPanel {

    protected PanelManager panelManager;

    protected CamposPanel camposPanel;
    protected BotoneraPanel botoneraPanel;

    public AbstractPantallaAltaPanel(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    protected void armarPanel() {
        this.inicializarCampos();
        this.botoneraPanel = new BotoneraPanel(getLabelBotonConfirmar());

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        this.add(camposPanel, BorderLayout.CENTER);
        this.add(botoneraPanel, BorderLayout.SOUTH);

        this.botoneraPanel.getBotonConfirmar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarAccionOk();
            }
        });

        this.botoneraPanel.getBotonCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarAccionCancel();
            }
        });
    }

    protected abstract void inicializarCampos();

    protected abstract String getLabelBotonConfirmar();

    protected abstract void ejecutarAccionOk();

    protected abstract void ejecutarAccionCancel();
}
