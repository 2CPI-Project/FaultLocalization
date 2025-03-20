import java.io.*;
import java.util.*;

public class classement {

    public static void main(String[] args) {
        String[] methods = {"Jaccard", "Ochiai", "Tarantula", "Zoltar", "exemple"};
        
        for (String method : methods) {
            String inputFileName = method + ".csv";
            String outputFileName = method + "_classement.csv";
            processFile(inputFileName, outputFileName);
        }
    }
    
    private static void processFile(String inputFile, String outputFile) {
        Map<Double, List<Integer>> valueToOffsets = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineNumber = 1; 
            while ((line = br.readLine()) != null) {
                double value = Double.parseDouble(line.trim());
                valueToOffsets.computeIfAbsent(value, k -> new ArrayList<>()).add(lineNumber);
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + inputFile);
            e.printStackTrace();
            return;
        }
        for (List<Integer> offsets : valueToOffsets.values()) {
            Collections.sort(offsets);
        }
        
        List<Double> values = new ArrayList<>(valueToOffsets.keySet());
        values.sort(Collections.reverseOrder());
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
            for (Double value : values) {
                List<Integer> offsets = valueToOffsets.get(value);
                String outputLine = joinList(offsets);
                pw.println(outputLine);
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + outputFile);
            e.printStackTrace();
        }
    }
    

    private static String joinList(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
