package org.example.DB;

public class Symptom {
    private int ID;
    private String name;

    public int getID() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Symptom(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    // getters and setters
}
