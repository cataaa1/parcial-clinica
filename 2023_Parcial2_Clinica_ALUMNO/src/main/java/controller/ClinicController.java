package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.text.DateFormatter;

import dataaccess.DBManager;
import model.MedicalAssurance;
import model.Medicine;
import model.Patient;
import model.prescriptions.Prescription;

public class ClinicController {
	private DBManager dbManager;
	private HashMap<Integer, Medicine> medicines;
	private HashMap<Integer, MedicalAssurance> medicalAssurances;
	private HashMap<Integer, Patient> patients;
	private ArrayList<Prescription> prescriptions;

	public ClinicController() {
		try {
			dbManager = new DBManager("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/clinic", "postgres", "1234");
			this.initialize();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void initialize() {
		medicines = new HashMap<Integer, Medicine>();
		patients = new HashMap<Integer, Patient>();
		medicalAssurances = new HashMap<Integer, MedicalAssurance>();
		prescriptions = new ArrayList<Prescription>();

		//TODO: completar las colecciones de medicinas, pacientes, coberturas medicas y prescripciones con los datos recuperados desde la Base de Datos 
		
	}

	public String costsReports() {
		
		StringBuilder sb = new StringBuilder();
		Statement st;
		sb.append("=============================================================================================================================================");
		sb.append("\n");
		sb.append("Costo de Prescripciones");
		sb.append("\n");
		sb.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");		
		sb.append("\n");
		sb.append("Paciente" + "\t" + "Prescripcion" + "\t\t\t\t\t\t\t" + "Costo");
		sb.append("\n");
		sb.append("=============================================================================================================================================");
		sb.append("\n");
		
		//Hash sugerido para obtener el total por medico
		HashMap<String, Double> professionalsCosts = new HashMap<>();

		//TODO: Completar el reporte de los costos de prescripciones a partir de la lista de prescripciones

		
    	sb.append("\n\n");
    	sb.append("Totales por Medico\n\n");
		//TODO: Mostrar los Totales por cobertura medica
		
		return sb.toString();
	}

}
