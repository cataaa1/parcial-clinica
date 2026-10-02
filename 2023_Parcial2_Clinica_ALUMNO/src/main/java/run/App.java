package run;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import controller.ClinicController;
import dataaccess.DBManager;
import view.PrescriptionsWindow;

public class App 
{
	public static void main(String[] args) throws Exception {
		try (Connection c = DBManager.getConnection();
			 Statement s = c.createStatement();
			 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM patients")) {
			rs.next();
			System.out.println("Conectado. Pacientes: " + rs.getInt(1));
		}
	}
        
}
