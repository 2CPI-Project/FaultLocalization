package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SuspiciousnessCalculator {
    private static final String INPUT_FILE = "src/packages/parametres.csv";
    private static final String OUTPUT_DIR = "src/packages/";

    public static void calculate(String formula) throws IOException {
        Path outputPath = Paths.get(OUTPUT_DIR + formula + ".csv");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE));
             PrintWriter writer = new PrintWriter(outputPath.toFile())) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 4) continue;

                int ep = Integer.parseInt(values[0].trim());
                int ef = Integer.parseInt(values[1].trim());
                int np = Integer.parseInt(values[2].trim());
                int nf = Integer.parseInt(values[3].trim());

                double score = switch (formula.toLowerCase()) {
                    case "ochiai" -> calculateOchiai(ef, ep, nf, np);
                    case "tarantula" -> calculateTarantula(ef, ep, nf, np);
                    case "jaccard" -> calculateJaccard(ef, ep, nf);
                    case "zoltar" -> calculateZoltar(ef, ep, nf, np);
                    default -> throw new IllegalArgumentException("Invalid formula: " + formula);
                };

                writer.println(score);
            }
        }
    }

    private static double calculateOchiai(int ef, int ep, int nf, int np) {
        double denominator = Math.sqrt((ef + nf) * (ef + ep));
        return denominator != 0 ? ef / denominator : 0;
    }

    private static double calculateTarantula(int ef, int ep, int nf, int np) {
        double failRatio = (ef + nf) != 0 ? (double) ef / (ef + nf) : 0;
        double passRatio = (ep + np) != 0 ? (double) ep / (ep + np) : 0;
        double total = failRatio + passRatio;
        return total != 0 ? failRatio / total : 0;
    }

    private static double calculateJaccard(int ef, int ep, int nf) {
        double denominator = ef + nf + ep;
        return denominator != 0 ? (double) ef / denominator : 0;
    }

    private static double calculateZoltar(int ef, int ep, int nf, int np) {
        if (ef == 0) return 0;
        double term = (10000.0 * nf * ep) / ef;
        return ef / (ef + nf + ep + term);
    }
}