package org.example.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseSearcher {
    private final DatabaseConnector connector;

    public DatabaseSearcher(DatabaseConnector connector) {
        this.connector = connector;
    }

    public Disease searchDiseaseById(int id) {
        Disease disease = null;
        try (PreparedStatement statement = connector.getConnection().prepareStatement("SELECT * FROM Diseases WHERE ID = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    disease = new Disease(resultSet.getInt("ID"), resultSet.getString("Name"), resultSet.getString("Treatment"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching for disease by ID: " + e.getMessage());
        }
        return disease;
    }

    public Disease searchDiseaseByName(String name) {
        Disease disease = null;
        try (PreparedStatement statement = connector.getConnection().prepareStatement("SELECT * FROM Diseases WHERE Name = ?")) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    disease = new Disease(resultSet.getInt("ID"), resultSet.getString("Name"), resultSet.getString("Treatment"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching for disease by name: " + e.getMessage());
        }
        return disease;
    }

    public List<String> getSymptomsForDisease(int diseaseId) {
        List<String> symptoms = new ArrayList<>();
        List<Integer> symptomIds = new ArrayList<>();
        String symptomQuery = "SELECT Name FROM Symptoms WHERE ID = ?";

        // First, get the symptom IDs from Disease_Symptoms table
        try (PreparedStatement statement = connector.getConnection().prepareStatement("SELECT Symptom_ID FROM Disease_Symptoms WHERE Disease_ID = ?")) {
            statement.setInt(1, diseaseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    symptomIds.add(resultSet.getInt("Symptom_ID"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting symptom IDs for disease ID: " + e.getMessage());
        }

        // Then, get the symptom names from Symptoms table
        for (int symptomId : symptomIds) {
            try (PreparedStatement statement = connector.getConnection().prepareStatement(symptomQuery)) {
                statement.setInt(1, symptomId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        symptoms.add(resultSet.getString("Name"));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error getting symptom name for symptom ID: " + symptomId + ", " + e.getMessage());
            }
        }

        return symptoms;
    }

    public List<Disease> getAllDiseases() {
        List<Disease> diseases = new ArrayList<>();
        try (PreparedStatement statement = connector.getConnection().prepareStatement("SELECT ID, Name FROM Diseases")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    diseases.add(new Disease(id, name));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting all diseases: " + e.getMessage());
        }
        return diseases;
    }
    public List<Symptom> getAllSymptoms() {
        List<Symptom> symptoms = new ArrayList<>();
        try (PreparedStatement statement = connector.getConnection().prepareStatement("SELECT * FROM Symptoms")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    symptoms.add(new Symptom(id, name));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting all diseases: " + e.getMessage());
        }
        return symptoms;
    }

    public Map<Integer, Set<String>> getAllDiseaseSymptoms() {
        Map<Integer, Set<String>> diseaseSymptomsMap = new HashMap<>();
        String query = "SELECT ds.Disease_ID, s.Name as Symptom_Name " +
                "FROM Disease_Symptoms ds " +
                "JOIN Symptoms s ON ds.Symptom_ID = s.ID";

        try {
            PreparedStatement statement = connector.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int diseaseId = resultSet.getInt("Disease_ID");
                String symptomName = resultSet.getString("Symptom_Name");

                diseaseSymptomsMap
                        .computeIfAbsent(diseaseId, k -> new HashSet<>())
                        .add(symptomName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return diseaseSymptomsMap;
    }


}
