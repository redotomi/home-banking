package UI.formularioUsuario;

import UI.PanelManager;
import entidades.Administrador;
import entidades.Cliente;
import entidades.Usuario;
import exceptions.UIExceptions.EntradaInvalidaException;
import exceptions.serviceExceptions.DniDuplicadoException;
import exceptions.serviceExceptions.ServiceException;
import service.CuentaService;
import service.MovimientoTarjetaService;
import service.TarjetaService;
import service.UsuarioService;

import javax.swing.*;

public class UsuarioFormPanel extends AbstractPantallaAltaPanel {

    private final UsuarioService usuarioService;
    private final CuentaService cuentaService;
    private final TarjetaService tarjetaService;
    private final MovimientoTarjetaService movimientoTarjetaService;
    private final Usuario usuarioAEditar;
    private final boolean modoRegistro;

    public UsuarioFormPanel(PanelManager panelManager, UsuarioService usuarioService, Usuario usuarioAEditar) {
        this(panelManager, usuarioService, null, null, null, usuarioAEditar, false);
    }

    public UsuarioFormPanel(PanelManager panelManager, UsuarioService usuarioService, Usuario usuarioAEditar, boolean modoRegistro) {
        this(panelManager, usuarioService, null, null, null, usuarioAEditar, modoRegistro);
    }

    public UsuarioFormPanel(PanelManager panelManager, UsuarioService usuarioService,
                            CuentaService cuentaService, TarjetaService tarjetaService,
                            MovimientoTarjetaService movimientoTarjetaService, Usuario usuarioAEditar) {
        this(panelManager, usuarioService, cuentaService, tarjetaService, movimientoTarjetaService, usuarioAEditar, false);
    }

    public UsuarioFormPanel(PanelManager panelManager, UsuarioService usuarioService,
                            CuentaService cuentaService, TarjetaService tarjetaService,
                            MovimientoTarjetaService movimientoTarjetaService,
                            Usuario usuarioAEditar, boolean modoRegistro) {
        super(panelManager);
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
        this.tarjetaService = tarjetaService;
        this.movimientoTarjetaService = movimientoTarjetaService;
        this.usuarioAEditar = usuarioAEditar;
        this.modoRegistro = modoRegistro;
        armarPanel();
    }

    @Override
    protected void inicializarCampos() {
        boolean editandoCliente = (usuarioAEditar instanceof Cliente) && (cuentaService != null);
        if (editandoCliente) {
            this.camposPanel = new EditarClientePanel(
                    usuarioAEditar, cuentaService, tarjetaService, movimientoTarjetaService);
        } else {
            this.camposPanel = new CamposUsuarioPanel(usuarioAEditar);
        }
    }

    @Override
    protected String getLabelBotonConfirmar() {
        return (usuarioAEditar == null) ? "Crear usuario" : "Guardar cambios";
    }

    @Override
    protected void ejecutarAccionOk() {
        CamposUsuarioPanel campos;
        if (this.camposPanel instanceof EditarClientePanel) {
            campos = ((EditarClientePanel) this.camposPanel).getCamposUsuarioPanel();
        } else {
            campos = (CamposUsuarioPanel) this.camposPanel;
        }

        try {
            int dni = parsearCampos(campos);
            String nombre = campos.getCampoNombre().getText().trim();
            String apellido = campos.getCampoApellido().getText().trim();

            if (usuarioAEditar == null) {
                String rolElegido = (String) campos.getComboRol().getSelectedItem();
                Usuario nuevoUsuario = "Administrador".equals(rolElegido)
                        ? new Administrador(0, nombre, apellido, dni)
                        : new Cliente(0, nombre, apellido, dni);

                usuarioService.agregarUsuario(nuevoUsuario);

                if (modoRegistro && nuevoUsuario instanceof Cliente) {
                    JOptionPane.showMessageDialog(this, "Cuenta creada correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                    panelManager.mostrarLoginPanel();
                } else {
                    panelManager.mostrarTablaUsuariosPanel();
                }
            } else {
                usuarioAEditar.setDni(dni);
                usuarioAEditar.setNombre(nombre);
                usuarioAEditar.setApellido(apellido);
                usuarioService.actualizarUsuario(usuarioAEditar);
                panelManager.mostrarTablaUsuariosPanel();
            }

        } catch (EntradaInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Entrada inválida", JOptionPane.WARNING_MESSAGE);
        } catch (DniDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese DNI.", "DNI duplicado", JOptionPane.WARNING_MESSAGE);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el usuario: " + ex.getMessage(), "Error del sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void ejecutarAccionCancel() {
        if (modoRegistro) {
            panelManager.mostrarLoginPanel();
        } else {
            panelManager.mostrarTablaUsuariosPanel();
        }
    }

    private int parsearCampos(CamposUsuarioPanel campos) throws EntradaInvalidaException {
        String dniTexto = campos.getCampoDni().getText().trim();
        String nombre   = campos.getCampoNombre().getText().trim();
        String apellido = campos.getCampoApellido().getText().trim();

        if (dniTexto.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            throw new EntradaInvalidaException("Completá todos los campos.");
        }
        try {
            return Integer.parseInt(dniTexto);
        } catch (NumberFormatException ex) {
            throw new EntradaInvalidaException("El DNI debe ser un número.");
        }
    }
}
