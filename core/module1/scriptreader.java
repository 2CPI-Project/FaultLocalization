import java.io.IOException;

public class scriptreader { 
//call a bash script in ur java code and run it 
  public static void main(String[] args) {
    String[] cmd = new String[] {"./gcovparser.sh"}; //your script here
    ProcessBuilder pb = new ProcessBuilder(cmd);
    try {
        Process p= pb.start();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}
