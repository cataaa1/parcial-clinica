package model.prescriptions;

import model.Medicine;
import model.Patient;

import java.math.BigDecimal;
import java.util.Date;

public class MedicinePrescription extends Prescription{
    private int dailyDose;
    private int days;
    private Medicine medicine;

    public MedicinePrescription(int prescription, Patient patient, Date date, String professional, Medicine m, int dailyDose, int days) {
        super(prescription, patient, date, professional);
        this.dailyDose = dailyDose;
        this.days = days;
        this.medicine = m;
    }

    public BigDecimal getPrescriptionCost() {
        return BigDecimal.valueOf(dailyDose).multiply(medicine.getUnitPrice()).multiply(BigDecimal.valueOf(days)) ;
    }

}
