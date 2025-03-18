import java.io.IOException;

public class ScriptReader2 {
//call a bash script in ur java code and run it
  public static void main(String[] args) {
    String[] cmd = new String[] {"./src/packages/categorizeTests_cases.sh"}; //your script here
    ProcessBuilder pb = new ProcessBuilder(cmd);

    try {
        Process p= pb.start();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}
