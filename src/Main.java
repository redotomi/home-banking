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
//        UsuarioDAO dao = new UsuarioDAOImplH2();
//
//        UsuarioService service = new UsuarioService(dao);
//
//        Usuario user = new Usuario("Jane", "DANE", 5555);
//
//        try {
//            service.actualizarUsuario(user);
//        } catch (ServiceException e) {
//            JOptionPane.showMessageDialog(null, "ERROR! =>" + e.getMessage());
//        }

//        TableManager tm = new TableManager();
//        tm.createUsuarioTable();
    }

    public void iniciarManager() {
        manager = new PanelManager(usuarioService);
        manager.armarManager();
        manager.mostrarTablaUsuariosPanel();
    }

    public void showFrame() {
        manager.showFrame();
    }
}
