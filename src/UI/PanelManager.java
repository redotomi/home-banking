package UI;

import UI.cliente.ClientePanel;
import UI.cliente.movimientos.ListaMovimientosPanel;
import UI.cliente.transferencias.ListaTransferenciasPanel;
import UI.cliente.transferencias.TransferenciaFormPanel;
import UI.formularioUsuario.UsuarioFormPanel;
import UI.login.LoginPanel;
import UI.tablaUsuarios.TablaUsuariosPanel;
import entidades.Cliente;
import entidades.Cuenta;
import entidades.MovimientoTarjeta;
import entidades.Tarjeta;
import entidades.Transferencia;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.CuentaService;
import service.MovimientoTarjetaService;
import service.TarjetaService;
import service.TransferenciaService;
import service.UsuarioService;

import java.util.List;

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

    private void cambiarPanel(JPanel panel, String titulo) {
        frame.getContentPane().removeAll();
        if (titulo != null) {
            frame.setTitle(titulo);
        }
        frame.getContentPane().add(panel);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
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
        cambiarPanel(tablaUsuariosPanel, "LaboBank - Admin");
    }

    public void mostrarFormularioUsuario(Usuario usuario) {
        UsuarioFormPanel formulario = new UsuarioFormPanel(
                this, usuarioService, cuentaService, tarjetaService, movimientoTarjetaService, usuario);
        cambiarPanel(formulario, null);
    }

    public void mostrarFormularioRegistro() {
        UsuarioFormPanel formulario = new UsuarioFormPanel(this, usuarioService, null, true);
        cambiarPanel(formulario, null);
    }

    public void mostrarLoginPanel() {
        cambiarPanel(new LoginPanel(this, usuarioService), "LaboBank");
    }

    public void mostrarPantallaCliente(Cliente cliente) {
        cambiarPanel(new ClientePanel(this, cliente, cuentaService, tarjetaService), "LaboBank - Mi cuenta");
    }

    public void mostrarFormularioTransferencia(Cuenta cuentaOrigen, Cliente cliente) {
        cambiarPanel(new TransferenciaFormPanel(cuentaOrigen, cliente, cuentaService, transferenciaService, this), "LaboBank - Nueva transferencia");
    }

    public void mostrarTransferenciasDeCliente(Cuenta cuenta, Cliente cliente) {
        List<Transferencia> transferencias;
        try {
            transferencias = transferenciaService.listarTransferenciasDeCuenta(cuenta.getCBU());
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(frame,
                    "No se pudieron cargar las transferencias: " + e.getMessage(),
                    "Error del sistema", JOptionPane.ERROR_MESSAGE);
            transferencias = List.of();
        }
        cambiarPanel(new ListaTransferenciasPanel(this, cliente, cuenta, transferencias), "LaboBank - Transferencias");
    }

    public void mostrarMovimientosDeTarjeta(Tarjeta tarjeta, Cliente cliente) {
        List<MovimientoTarjeta> movimientos;
        try {
            movimientos = movimientoTarjetaService.listarMovimientosPorTarjeta(tarjeta.getNumero());
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(frame,
                    "No se pudieron cargar los movimientos: " + e.getMessage(),
                    "Error del sistema", JOptionPane.ERROR_MESSAGE);
            movimientos = List.of();
        }
        cambiarPanel(new ListaMovimientosPanel(this, cliente, tarjeta, movimientos), "LaboBank - Movimientos");
    }
}
