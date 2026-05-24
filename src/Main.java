import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImplH2;
import entidades.Usuario;
import exceptions.serviceExceptions.ServiceException;
import service.UsuarioService;
import util.TableManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAOImplH2();

        UsuarioService service = new UsuarioService(dao);

        Usuario user = new Usuario("Jane", "DANE", 5555);

        try {
            service.actualizarUsuario(user);
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(null, "ERROR! =>" + e.getMessage());
        }

    }
}
