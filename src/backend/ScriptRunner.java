package backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScriptRunner {
    private static final boolean IS_WINDOWS = 
        System.getProperty("os.name").toLowerCase().contains("win");

    public static void runOutputGenerator() throws IOException, InterruptedException {
        String scriptPath = new File("src/packages/outputGenerator.sh").getAbsolutePath();
        
        ProcessBuilder pb;
        if (IS_WINDOWS) {
        	String unixPath = scriptPath.toString()
                    .replace("\\", "/")
                    .replace(" ", "\\ ")
                    .replace("C:", "/mnt/c");
                
             pb = new ProcessBuilder("bash", "-c", unixPath);
        } else {
            pb = new ProcessBuilder("sh", scriptPath);
        }
        
        pb.redirectErrorStream(true); // Merge error/output streams
        Process p = pb.start();
        
        // Read output for debugging
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[SCRIPT] " + line);
            }
        }
        
        p.waitFor();
    }
    public static void runCategorizer() throws IOException, InterruptedException {
        String scriptPath = new File("src/packages/categorizeTests_cases.sh").getAbsolutePath();
        
        ProcessBuilder pb;
        if (IS_WINDOWS) {
        	String unixPath = scriptPath.toString()
                    .replace("\\", "/")
                    .replace(" ", "\\ ")
                    .replace("C:", "/mnt/c");
                
             pb = new ProcessBuilder("bash", "-c", unixPath);
        } else {
            pb = new ProcessBuilder("sh", scriptPath);
        }
        
        pb.redirectErrorStream(true); // Merge error/output streams
        Process p = pb.start();
        
        // Read output for debugging
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[SCRIPT] " + line);
            }
        }
        p.waitFor();
    }
}