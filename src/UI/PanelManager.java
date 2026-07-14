package UI;

import UI.cliente.ClientePanel;
import UI.cliente.TransferenciaFormPanel;
import UI.formularioUsuario.UsuarioFormPanel;
import UI.login.LoginPanel;
import UI.tablaUsuarios.TablaUsuariosPanel;
import entidades.Cliente;
import entidades.Cuenta;
import entidades.Usuario;
import service.CuentaService;
import service.MovimientoTarjetaService;
import service.TarjetaService;
import service.TransferenciaService;
import service.UsuarioService;

import javax.swing.*;

public class PanelManager {
    private JFrame frame;
    private UsuarioService usuarioService;
    private CuentaService cuentaService;
    private TarjetaService tarjetaService;
    private TransferenciaService transferenciaService;
    private MovimientoTarjetaService movimientoTarjetaService;

    private TablaUsuariosPanel tablaUsuariosPanel;

    public PanelManager(UsuarioService usuarioService, CuentaService cuentaService,
                        TarjetaService tarjetaService, TransferenciaService transferenciaService,
                        MovimientoTarjetaService movimientoTarjetaService) {
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
        this.tarjetaService = tarjetaService;
        this.transferenciaService = transferenciaService;
        this.movimientoTarjetaService = movimientoTarjetaService;
    }

    public void armarManager() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tablaUsuariosPanel = new TablaUsuariosPanel(this, usuarioService);
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    public void mostrarTablaUsuariosPanel() {
        frame.getContentPane().removeAll();
        frame.setTitle("LaboBank - Admin");
        frame.getContentPane().add(tablaUsuariosPanel);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarFormularioUsuario(Usuario usuario) {
        frame.getContentPane().removeAll();
        UsuarioFormPanel formulario = new UsuarioFormPanel(
                this, usuarioService, cuentaService, tarjetaService, movimientoTarjetaService, usuario);
        frame.getContentPane().add(formulario);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarFormularioRegistro() {
        frame.getContentPane().removeAll();
        UsuarioFormPanel formulario = new UsuarioFormPanel(this, usuarioService, null, true);
        frame.getContentPane().add(formulario);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarLoginPanel() {
        frame.getContentPane().removeAll();
        frame.setTitle("LaboBank");
        frame.getContentPane().add(new LoginPanel(this, usuarioService));
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarPantallaCliente(Cliente cliente) {
        frame.getContentPane().removeAll();
        frame.setTitle("LaboBank - Mi cuenta");
        frame.getContentPane().add(new ClientePanel(this, cliente, cuentaService, transferenciaService));
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }

    public void mostrarFormularioTransferencia(Cuenta cuentaOrigen, Cliente cliente) {
        frame.getContentPane().removeAll();
        frame.setTitle("LaboBank - Nueva transferencia");
        frame.getContentPane().add(new TransferenciaFormPanel(cuentaOrigen, cliente, cuentaService, transferenciaService, this));
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }
}
