package org.example;

import java.util.Map;


public class TrainingData {

    private int diseaseID;
    private String diseaseName;
    private Map<String, Integer> symptomVector;

    public TrainingData(int diseaseID, String diseaseName, Map<String, Integer> symptomVector) {
        this.diseaseID = diseaseID;
        this.diseaseName = diseaseName;
        this.symptomVector = symptomVector;
    }

    public int getDiseaseID() {
        return diseaseID;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public Map<String, Integer> getSymptomVector() {
        return symptomVector;
    }
}