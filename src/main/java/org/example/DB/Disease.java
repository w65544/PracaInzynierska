package org.example.DB;

public class Disease {
    private  int ID;
    private  String name;
    private  String treatment;

    public Disease(int ID, String name, String treatment) {
        this.ID = ID;
        this.name = name;
        this.treatment = treatment;
    }

    public Disease(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
