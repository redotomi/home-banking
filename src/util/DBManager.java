package util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBManager {
	
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_USERNAME = "sa";
	private static final String DB_PASSWORD = "";



	public static Connection connect() {
		Connection c = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

		try {
			String url = "jdbc:h2:tcp://localhost//"+obtenerUbicacionBase();
			//System.out.println(url);
			c = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);
			c.setAutoCommit(false);
		} catch (SQLException e) {
            e.printStackTrace();
			System.exit(0);
		}

		return c;
	}

	private static String getDirectorioBase() {
		File currDir = new File("h2/base_de_datos/");
		return currDir.getAbsolutePath();
	}

	public static String obtenerUbicacionBase() {
        String ubicacion = getDirectorioBase() + "/REDONDO"; //******REEMPLAZAR POR APELLIDO!!
        File dbFile = new File(ubicacion + ".mv.db");
        File parentDir = dbFile.getParentFile();
        try {
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (dbFile.createNewFile()) {
                System.out.println("Archivo de base de datos '" + dbFile.getName() + "' creado exitosamente.");
            } else {
                System.out.println("La base de datos ya existía en el disco.");
            }
        } catch (Exception e) {
            System.out.println("HAY UN PROBLEMA CON LA CREACION DEL ARCHIVO DE BASE DE DATOS. NO SE PUEDE CONTINUAR");
            System.exit(0);
        }

		return ubicacion;
	}
	
	
	
	/*
	public static Connection connect() {
		Connection c = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			System.exit(0);
		}
		try {
			c = DriverManager.getConnection(DB_BASE_URL, DB_USERNAME, DB_PASSWORD);
			c.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return c;
	}
	
	
	private static String getDirectorioBase() {
		File currDir = new File("h2/baseDeDatos/");
		return currDir.getAbsolutePath();
	}
	
	public static String obtenerUbicacionBase() {
		String url = "jdbc:h2:tcp://localhost//{DIR}/ejemplo";
		url = url.replace("{DIR}", getDirectorioBase());
		return url;
	}
	*/
}
