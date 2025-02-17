import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class outputReader {
    public static void main(String[] arg) {

        // Step 1: Read path1 and store its lines in a list

        String path1 = "/home/kali/DebuggerJava/results.csv"; // Path to results of passing or failing test cases
        List<Boolean> results = new ArrayList<>();

        try (BufferedReader reader1 = new BufferedReader(new FileReader(path1))) {
            String line;

            while ((line = reader1.readLine()) != null) {
                results.add(line.trim().equals("P")); 
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + path1);
            e.printStackTrace();
        } 

        // Step 2: Read path2 and store its lines in a list

        String path2 = "/home/kali/DebuggerJava/output.csv"; // Path to the set of instructions executed
        List<String> linesFromFile = new ArrayList<>();

        try (BufferedReader reader2 = new BufferedReader(new FileReader(path2))) {
            String line;

            while ((line = reader2.readLine()) != null) {
                linesFromFile.add(line); // Store all lines in a list
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + path2);
            e.printStackTrace();
        } 

        // Step 3: store in parametres.csv

        String outputPath = "/home/kali/DebuggerJava/parametres.csv"; // Path to your output file *****here
        try (FileWriter writer = new FileWriter(outputPath)) {

            // Step 4: Process each value of i (from 1 to 8)

            for (int i = 1; i <= 172; i++) {  // Number of lines in the code *****here
                int ep = 0, ef = 0, np = 0, nf = 0; 

                for (int index = 0; index < results.size() && index < linesFromFile.size(); index++) {
                    String line = linesFromFile.get(index);
                    String[] values = line.split(",\\s*"); // Split values by comma and optional spaces
                    boolean test = Arrays.asList(values).contains(String.valueOf(i));

                    if (test && results.get(index)) {
                        ep++;
                    } else if (test && !results.get(index)) {
                        ef++;
                    } else if (!test && results.get(index)) {
                        np++;
                    } else if (!test && !results.get(index)) {
                        nf++;
                    }
                }

                // Step 5: Write results for the current iteration to the file
                String outputLine = ep + "," + ef + "," + np + "," + nf + "\n"; 
                writer.write(outputLine);

            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + outputPath);
            e.printStackTrace();
        } 
    }
}
