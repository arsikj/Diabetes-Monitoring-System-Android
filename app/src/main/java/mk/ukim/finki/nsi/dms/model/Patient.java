package mk.ukim.finki.nsi.dms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejan on 03.9.2017.
 */

public class Patient {

    private int id;
    private String name;
    private String username;
    private String password;
    private List<Measure> measures;


    public Patient() {

    }

    public Patient(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.measures = new ArrayList<Measure>();
    }

    public Patient(String name, String username, String password, List<Measure> measures){
        this.name = name;
        this.username = username;
        this.password = password;
        this.measures = measures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }
}
