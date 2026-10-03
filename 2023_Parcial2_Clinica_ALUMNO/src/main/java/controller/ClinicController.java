package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.text.DateFormatter;

import dataaccess.DBManager;
import model.MedicalAssurance;
import model.Medicine;
import model.Patient;
import model.prescriptions.MedicinePrescription;
import model.prescriptions.Prescription;
import model.prescriptions.StudyPrescription;

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
	
	private void initialize() throws SQLException {
		medicines = new HashMap<Integer, Medicine>();
		patients = new HashMap<Integer, Patient>();
		medicalAssurances = new HashMap<Integer, MedicalAssurance>();
		prescriptions = new ArrayList<Prescription>();

		loadMedicalAssurances();
		loadMedicines();
		loadPatients();
		loadPrescriptions();

		System.out.println("Obras sociales: " + medicalAssurances.size());
		System.out.println("Medicinas: " + medicines.size());
		System.out.println("Pacientes: " + patients.size());
		System.out.println("Prescripciones: " + prescriptions.size());
		//TODO: completar las colecciones de medicinas, pacientes, coberturas medicas y prescripciones con los datos recuperados desde la Base de Datos 
		
	}

	private void loadPrescriptions() throws SQLException {
		String sql = "SELECT prescription, date, professional, patient, prescriptiontype,dailydose, days, medicine, studyrequired, diagnosis FROM prescription";
		try(Statement stmt = dbManager.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql)){
			while (rs.next()){
				int id = rs.getInt("prescription");
				Patient p = patients.get(rs.getInt("patient"));
				if (rs.getString("prescriptiontype").equals("M")) {
					Medicine m = medicines.get(rs.getInt("medicine"));
					prescriptions.add(new MedicinePrescription(id, p, rs.getDate("date"), rs.getString("professional"), m, rs.getInt("dailydose"), rs.getInt("days")));
				}else{
					prescriptions.add(new StudyPrescription(id,p,rs.getDate("date"), rs.getString("professional"), rs.getString("studyrequired"),rs.getString("diagnosis")));
				}
			}
		}
	}

	private void loadPatients() throws SQLException{
		String sql = "SELECT patient, name, birthdate, medicalassurance FROM patient";
		try(Statement stmt = dbManager.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql)){
			while (rs.next()){
				MedicalAssurance ma = medicalAssurances.get(rs.getInt("medicalassurance"));
				Patient p = new Patient(rs.getInt("patient"),rs.getString("name"),rs.getDate("birthdate"),ma);
				patients.put(p.getPatient(),p);
			}
		}
	}

	private void loadMedicalAssurances() throws SQLException{
		String sql = "SELECT medicalassurance, name FROM medicalassurance";
		try(Statement stmt = dbManager.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql)){
			while (rs.next()){
				MedicalAssurance ma = new MedicalAssurance(rs.getInt("medicalassurance"),rs.getString("name"));
				medicalAssurances.put(ma.getMedicalAssurance(),ma);
			}
		}
	}

	private void loadMedicines() throws SQLException{
		String sql = "SELECT medicine, description, unitprice::numeric FROM medicine";
		try(Statement stmt = dbManager.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql)){
			while (rs.next()){
				Medicine m = new Medicine(rs.getInt("medicine"),rs.getString("description"),rs.getBigDecimal("unitprice"));
				medicines.put(m.getMedicine(),m);
			}
		}


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
		
		//Hash sugerido para obtener el total por médico
		HashMap<String, Double> professionalsCosts = new HashMap<>();

		List<Prescription> copia = new ArrayList<>(prescriptions);
		Collections.sort(copia);

		Patient anterior = null;
		for (Prescription p : copia){
			if (!p.getPatient().equals(anterior)){
				sb.append("\n").append(p.getPatient()).append("\n");
				anterior = p.getPatient();
			}
			sb.append("\t").append(p).append("\t").append(p.getPrescriptionCost()).append("\n");
			professionalsCosts.merge(p.getProfessional(), p.getPrescriptionCost().doubleValue(), Double::sum);
		}

		
    	sb.append("\n\n");
    	sb.append("Totales por Medico\n\n");
		for (Map.Entry<String, Double> e : professionalsCosts.entrySet()) {
			sb.append("\t").append(e.getKey()).append(": $").append(e.getValue()).append("\n");
		}
		
		return sb.toString();
	}

}
