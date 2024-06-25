package org.example.Algorithms;

import org.example.DB.DatabaseConnector;
import org.example.DB.DatabaseSearcher;
import org.example.DB.Disease;
import org.example.DB.Symptom;
import org.example.TrainingData;

import java.util.*;

public class DiseaseMatcher {

    private DatabaseConnector connector;
    private DatabaseSearcher searcher;
    private List<String> userSymptoms;

    public DiseaseMatcher(List<String> userSymptoms) {
        this.connector = new DatabaseConnector();
        this.searcher = new DatabaseSearcher(connector);
        this.userSymptoms = userSymptoms;
    }

    public void matchAndPrintMostProbableDisease() {
        List<Disease> allDiseases = searcher.getAllDiseases();
        List<Symptom> allSymptoms = searcher.getAllSymptoms();
        Map<Integer, Set<String>> diseaseSymptomsMap = searcher.getAllDiseaseSymptoms();

        List<TrainingData> trainingData = prepareTrainingData(allDiseases, allSymptoms, diseaseSymptomsMap);

        Map<Integer, Double> diseaseMatchScore = calculateDiseaseMatchScores(trainingData);

        printDiseaseMatchScores(diseaseMatchScore, allDiseases);

        connector.closeConnection();
    }

    private List<TrainingData> prepareTrainingData(List<Disease> allDiseases, List<Symptom> allSymptoms, Map<Integer, Set<String>> diseaseSymptomsMap) {
        List<TrainingData> trainingData = new ArrayList<>();
        for (Disease disease : allDiseases) {
            Map<String, Integer> symptomVector = new HashMap<>();
            Set<String> symptomsForDisease = diseaseSymptomsMap.get(disease.getID());
            if (symptomsForDisease != null) {
                for (Symptom symptom : allSymptoms) {
                    if (symptomsForDisease.contains(symptom.getName())) {
                        symptomVector.put(symptom.getName(), 1);
                    }
                }
            }
            trainingData.add(new TrainingData(disease.getID(), disease.getName(), symptomVector));
        }
        return trainingData;
    }

    private Map<Integer, Double> calculateDiseaseMatchScores(List<TrainingData> trainingData) {
        Map<Integer, Double> diseaseMatchScore = new HashMap<>();

        for (TrainingData data : trainingData) {
            double matchScore = 0;
            Map<String, Integer> symptomVector = data.getSymptomVector();
            for (String userSymptom : userSymptoms) {
                if (symptomVector.containsKey(userSymptom)) {
                    int index = new ArrayList<>(symptomVector.keySet()).indexOf(userSymptom);
                    double weight = 1.0 - (index * 0.1);
                    matchScore += Math.max(weight, 0.1);
                }
            }
            diseaseMatchScore.put(data.getDiseaseID(), matchScore);
        }
        return diseaseMatchScore;
    }

    private void printDiseaseMatchScores(Map<Integer, Double> diseaseMatchScore, List<Disease> allDiseases) {
        System.out.println("\nDisease Match Scores:");
        for (Map.Entry<Integer, Double> entry : diseaseMatchScore.entrySet()) {
            String diseaseName = allDiseases.stream()
                    .filter(disease -> disease.getID() == entry.getKey())
                    .map(Disease::getName)
                    .findFirst()
                    .orElse("Unknown Disease");
            String formattedScore = String.format("%.1f", entry.getValue());
            System.out.println("Disease ID: " + entry.getKey() + ", Disease: " + diseaseName + ", Match Score: " + formattedScore);
        }

        Optional<Map.Entry<Integer, Double>> maxEntry = diseaseMatchScore.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent()) {
            String probableDisease = allDiseases.stream()
                    .filter(disease -> disease.getID() == maxEntry.get().getKey())
                    .map(Disease::getName)
                    .findFirst()
                    .orElse("Unknown Disease");
            String formattedMaxScore = String.format("%.1f", maxEntry.get().getValue());
            System.out.println("\nThe most probable disease is: " + probableDisease + " with a match score of " + formattedMaxScore);
        } else {
            System.out.println("\nNo matching disease found.");
        }
    }
}
