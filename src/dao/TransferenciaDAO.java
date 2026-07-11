package dao;

import entidades.Transferencia;
import exceptions.DAOExceptions.DAOException;

import java.util.List;

public interface TransferenciaDAO {
    void crearTransferencia(Transferencia unaTransferencia) throws DAOException;

    Transferencia muestraTransferencia(int idTransferencia) throws DAOException;

    List<Transferencia> listaTransferenciasUsuario(int dniUsuario) throws DAOException;
}
