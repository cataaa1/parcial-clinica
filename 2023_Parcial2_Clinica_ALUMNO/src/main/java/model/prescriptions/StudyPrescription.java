package model.prescriptions;

import model.Patient;

import java.math.BigDecimal;
import java.util.Date;

public class StudyPrescription extends Prescription{
    private String requiredStudy;
    private String diagnosis;

    public StudyPrescription(int prescription, Patient patient, Date date, String professional, String requiredStudy, String diagnosis) {
        super(prescription, patient, date, professional);
        this.requiredStudy = requiredStudy;
        this.diagnosis = diagnosis;
    }

    @Override
    public BigDecimal getPrescriptionCost() {
        return BigDecimal.valueOf(1000);
    }
}
