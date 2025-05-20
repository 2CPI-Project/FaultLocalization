package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParameterGenerator {
    public static void generateParameters() throws IOException {
        // Read test results (P/F)
        List<Boolean> results = new ArrayList<>();
        try (BufferedReader resultsReader = new BufferedReader(new FileReader("src/packages/results.csv"))) {
            String line;
            while ((line = resultsReader.readLine()) != null) {
                results.add(line.trim().equals("P"));
            }
        }

        // Read coverage data from output.csv
        List<String[]> coverageData = new ArrayList<>();
        try (BufferedReader coverageReader = new BufferedReader(new FileReader("src/packages/output.csv"))) {
            String line;
            while ((line = coverageReader.readLine()) != null) {
                coverageData.add(line.split(","));
            }
        }

        // Get number of lines in tcas_Incorrect.c
        int numberOfLines = countLines("src/packages/tcas_Incorrect.c");

        // Generate parameters for each line
        try (FileWriter writer = new FileWriter("src/packages/parametres.csv")) {
            for (int lineNumber = 1; lineNumber <= numberOfLines; lineNumber++) {
                int ep = 0, ef = 0, np = 0, nf = 0;
                
                for (int testIndex = 0; testIndex < results.size(); testIndex++) {
                    boolean testPassed = results.get(testIndex);
                    String[] coverage = coverageData.get(testIndex);
                    boolean lineCovered = false;

                    // Check if current line is in coverage data
                    for (String coveredLine : coverage) {
                        if (coveredLine.trim().equals(String.valueOf(lineNumber))) {
                            lineCovered = true;
                            break;
                        }
                    }

                    // Update counts based on coverage and test result
                    if (lineCovered) {
                        if (testPassed) ep++;
                        else ef++;
                    } else {
                        if (testPassed) np++;
                        else nf++;
                    }
                }

                writer.write(ep + "," + ef + "," + np + "," + nf + "\n");
            }
        }
    }

    private static int countLines(String filename) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.readLine() != null) count++;
        }
        return count;
    }
}