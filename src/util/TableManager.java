package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableManager {
	
	public void createUsuarioTable() {
		
		Connection c = DBManager.connect();
		
		String sql = "CREATE TABLE usuarios (id IDENTITY, nombre VARCHAR(256), apellido VARCHAR(256), dni INTEGER UNIQUE, rol varchar(256))";
		
		try {
			Statement s = c.createStatement();
			s.execute(sql);
		} catch (SQLException e) {
			try {
				c.rollback();
				e.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
	}
	
	
	public void dropUsuarioTable() {
		
		Connection c = DBManager.connect();
		
		String sql = "DROP TABLE usuarios";
		
		try {
			Statement s = c.createStatement();
			s.execute(sql);
			c.commit();
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
