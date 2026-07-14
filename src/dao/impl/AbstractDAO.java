package dao.impl;

import exceptions.DAOExceptions.ConexionDAOException;
import util.DBManager;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO {
    
    protected Connection obtenerConexion() throws ConexionDAOException {
        Connection c = DBManager.connect();
        if (c == null) {
            throw new ConexionDAOException("No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    protected void cerrarConexion(Connection c) {
        try {
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
