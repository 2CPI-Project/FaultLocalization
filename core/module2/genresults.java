import java.io.*;
import java.util.*;

public class genresults {

    public static Set<String> readFileToSet(String filename) throws IOException {
        Set<String> set = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            set.add(line.trim());  
        }
        reader.close();
        return set;
    }

    public static void main(String[] args) throws IOException {
        String cas_tests = "new-cases.txt";
        String failing_tests = "Failing_test_cases.txt";
        String passing_tests = "Passing_test_cases.txt";
        String csv_file = "results.csv";
        
        Set<String> failingTests = readFileToSet(failing_tests);
        Set<String> passingTests = readFileToSet(passing_tests);
        
        BufferedReader reader = new BufferedReader(new FileReader(cas_tests));
        FileWriter writer = new FileWriter(csv_file);
        
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (failingTests.contains(line)) {
                writer.append("F\n"); 
            } else if (passingTests.contains(line)) {
                writer.append("P\n"); 
            } else {
                writer.append("MAKACH\n");  
            }
        }
        
        reader.close();
        writer.close();
    }
}
