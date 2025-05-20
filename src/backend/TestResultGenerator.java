package backend;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class TestResultGenerator {
    public static void generateResults() throws IOException {
        Set<String> failingTests = readFileToSet("src/packages/Failing_test_cases.txt");
        Set<String> passingTests = readFileToSet("src/packages/Passing_test_cases.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/packages/cas-tests.txt"));
             FileWriter writer = new FileWriter("src/packages/results.csv")) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (failingTests.contains(line)) writer.append("F\n");
                else if (passingTests.contains(line)) writer.append("P\n");
                else writer.append("MAKACH\n");
            }
        }
    }

    private static Set<String> readFileToSet(String filename) throws IOException {
        Set<String> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) set.add(line.trim());
        }
        return set;
    }
}