import UI.PanelManager;
import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImplH2;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.UsuarioService;
import util.TableManager;

import javax.swing.*;

public class Main {
    UsuarioDAOImplH2 usuarioDAO = new UsuarioDAOImplH2();
    UsuarioService usuarioService = new UsuarioService(usuarioDAO);

    private PanelManager manager;

    public static void main(String[] args) {

        Main main = new Main();
        main.iniciarManager();
        main.showFrame();
    }

    public void iniciarManager() {
        manager = new PanelManager(usuarioService);
        manager.armarManager();
        manager.mostrarLoginPanel();
    }

    public void showFrame() {
        manager.showFrame();
    }
}
