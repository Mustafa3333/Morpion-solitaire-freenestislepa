package org.netislepafree.morpion_solitaire.model.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Score {
    private static final String scorePath = "history.csv";
    
    public static Map<String, Integer> getScores() throws IOException {
        File file = new File(scorePath);
        Map<String, Integer> scoreMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                int commaIndex = line.indexOf(',');
                
                if (commaIndex != -1) { 
                    String name = line.substring(0, commaIndex);
                    int score = Integer.parseInt(line.substring(commaIndex + 1));
                    scoreMap.put(name, score);
                } else {
                    System.err.println("Ligne invalide ignorée : " + line);
                }
            }
        } catch (IOException e) {
            throw e;
        }

        return scoreMap;
    }


    public static void saveScore(String name, Double score) throws IOException {
        try (BufferedWriter out = new BufferedWriter(
                new FileWriter(scorePath, true))) {
            out.write(name + "," + score + "\n");
        }
    }
}
