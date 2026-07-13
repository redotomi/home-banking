package service;

import dao.TarjetaDAO;
import entidades.Tarjeta;
import entidades.Usuario;
import exceptions.DAOExceptions.DAOException;
import exceptions.serviceExceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class TarjetaService {

    private final TarjetaDAO tarjetaDAO;
    private final Random random = new Random();

    public TarjetaService(TarjetaDAO tarjetaDAO) {
        this.tarjetaDAO = tarjetaDAO;
    }

    public void crearTarjeta(Usuario usuario, String proveedor, int limite) throws ServiceException {
        String numero        = generarNumeroTarjeta();
        String cvv           = generarCVV(proveedor);
        LocalDate vencimiento = LocalDate.now().plusYears(5);
        String nombreTitular  = usuario.getNombre() + " " + usuario.getApellido();
        Tarjeta tarjeta = new Tarjeta(numero, limite, 0, proveedor, vencimiento,
                                      cvv, nombreTitular, String.valueOf(usuario.getDni()));
        try {
            tarjetaDAO.crearTarjeta(tarjeta);
        } catch (DAOException e) {
            throw new ServiceException("Error al crear la tarjeta para el usuario con DNI: " + usuario.getDni(), e);
        }
    }

    public List<Tarjeta> listarTarjetasDeCliente(String dniUsuario) throws ServiceException {
        try {
            return tarjetaDAO.listarTarjetasPorUsuario(dniUsuario);
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener las tarjetas del cliente con DNI: " + dniUsuario, e);
        }
    }

    private String generarNumeroTarjeta() {
        StringBuilder sb = new StringBuilder();
        for (int grupo = 0; grupo < 4; grupo++) {
            if (grupo > 0) sb.append(" ");
            for (int i = 0; i < 4; i++) {
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }

    private String generarCVV(String proveedor) {
        int digitos = proveedor.equals("AMEX") ? 4 : 3;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digitos; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
