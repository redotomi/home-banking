import UI.PanelManager;
import dao.impl.CuentaDAOImplH2;
import dao.impl.TransferenciaDAOImplH2;
import dao.impl.UsuarioDAOImplH2;
import service.CuentaService;
import service.TransferenciaService;
import service.UsuarioService;
import util.TableManager;

import javax.swing.*;

public class Main {
    UsuarioDAOImplH2 usuarioDAO = new UsuarioDAOImplH2();
    CuentaDAOImplH2 cuentaDAO = new CuentaDAOImplH2();
    TransferenciaDAOImplH2 transferenciaDAO = new TransferenciaDAOImplH2();
    CuentaService cuentaService = new CuentaService(cuentaDAO);
    UsuarioService usuarioService = new UsuarioService(usuarioDAO, cuentaService);
    TransferenciaService transferenciaService = new TransferenciaService(transferenciaDAO, cuentaService);

    private PanelManager manager;

    public static void main(String[] args) {

        Main main = new Main();
        main.iniciarManager();
        main.showFrame();
    }

    public void iniciarManager() {
        manager = new PanelManager(usuarioService, cuentaService, transferenciaService);
        manager.armarManager();
        manager.mostrarLoginPanel();
    }

    public void showFrame() {
        manager.showFrame();
    }
}
