package org.example.DB;

public class DiseaseSymptom {
    private int diseaseId;
    private int symptomId;

    public DiseaseSymptom(int diseaseId, int symptomId) {
        this.diseaseId = diseaseId;
        this.symptomId = symptomId;
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public int getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(int symptomId) {
        this.symptomId = symptomId;
    }
}
