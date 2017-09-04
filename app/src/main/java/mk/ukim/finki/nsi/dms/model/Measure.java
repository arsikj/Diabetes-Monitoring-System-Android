package mk.ukim.finki.nsi.dms.model;

import java.util.Date;

/**
 * Created by dejan on 03.9.2017.
 */

public class Measure {

    private int id;
    private int level;
    private Date dateAdded;
    private Patient patient;

    public Measure(){

    }

    public Measure(int level, Date dateAdded){
        this.level = level;
        this.dateAdded = dateAdded;
        this.patient = new Patient();
    }

    public Measure(int level, Date dateAdded, Patient patient) {
        this.level = level;
        this.dateAdded = dateAdded;
        this.patient = patient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
