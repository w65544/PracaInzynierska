package org.example;

import org.example.Algorithms.DiseaseMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter user symptoms (comma-separated):");
        String userInput = scanner.nextLine().trim();

        List<String> userSymptoms = Arrays.asList(userInput.split("\\s*,\\s*"));

        DiseaseMatcher diseaseMatcher = new DiseaseMatcher(userSymptoms);
        diseaseMatcher.matchAndPrintMostProbableDisease();

        scanner.close();

    }
}