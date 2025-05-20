package backend;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ClassementGenerator {
    private static final String INPUT_DIR = "src/packages/";
    private static final String OUTPUT_DIR = "src/packages/classements/";

    public static void generateAllClassements() throws IOException {
        // Only generate for existing formula files
        String[] formulas = {"Ochiai", "Tarantula", "Jaccard", "Zoltar"};
        for (String formula : formulas) {
            Path formulaFile = Path.of(INPUT_DIR + formula + ".csv");
            if (Files.exists(formulaFile)) {
                generateClassement(formula);
            }
        }
    }

    public static void generateClassement(String formula) throws IOException {
        Path inputPath = Path.of(INPUT_DIR + formula + ".csv");
        Path outputPath = Path.of(OUTPUT_DIR + formula + "_classement.csv");
        
        if (!Files.exists(inputPath)) {
            throw new FileNotFoundException("Formula file not found: " + inputPath);
        }

        TreeMap<Double, List<Integer>> valueMap = new TreeMap<>(Comparator.reverseOrder());

        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                try {
                    double value = Double.parseDouble(line.trim());
                    valueMap.computeIfAbsent(value, k -> new ArrayList<>())
                            .add(lineNumber);
                    lineNumber++;
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid value at line " + lineNumber);
                }
            }
        }

        Files.createDirectories(outputPath.getParent());
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputPath))) {
            for (Map.Entry<Double, List<Integer>> entry : valueMap.entrySet()) {
                List<Integer> sortedLines = entry.getValue();
                Collections.sort(sortedLines);
                writer.println(String.join(",", 
                    sortedLines.stream()
                        .map(String::valueOf)
                        .toArray(String[]::new)
                ));
            }
        }
    }
}