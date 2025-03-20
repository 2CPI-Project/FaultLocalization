import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputReader {
    public static void main(String[] arg) {

        // Step 1: Read path1 (results.csv)
        String path1 = "./src/packages/results.csv";
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

        // Step 2: Read path2 (new-output.csv)
        String path2 = "./src/packages/output.csv";
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

        // Step 3: Read tcas_Incorrect.c to determine the number of lines
        String tcasPath = "./src/packages/tcas_Incorrect.c";
        int numberOfLines = 0;

        try (BufferedReader tcasReader = new BufferedReader(new FileReader(tcasPath))) {
            while (tcasReader.readLine() != null) {
                numberOfLines++; // Count the number of lines
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + tcasPath);
            e.printStackTrace();
        }

        // Step 4: Store results in parametres.csv
        String outputPath = "./src/packages/parametres.csv";
        try (FileWriter writer = new FileWriter(outputPath)) {

            // Step 5: Process each instruction (from 1 to numberOfLines)
            for (int i = 1; i <= numberOfLines; i++) {
                int ep = 0, ef = 0, np = 0, nf = 0;

                for (int index = 0; index < results.size() && index < linesFromFile.size(); index++) {
                    String line = linesFromFile.get(index);
                    String[] values = line.split(","); 

                    boolean test = false;
                    for (String val : values) {
                        if (val.trim().equals(String.valueOf(i))) { 
                            test = true;
                            break;
                        }
                    }

                    if (test && results.get(index)) ep++;
                    else if (test && !results.get(index)) ef++;
                    else if (!test && results.get(index)) np++;
                    else if (!test && !results.get(index)) nf++;
                }

                // Step 6: Write results to file
                writer.write(ep + "," + ef + "," + np + "," + nf + "\n");
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + outputPath);
            e.printStackTrace();
        }
    }
}
