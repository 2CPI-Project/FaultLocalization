import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;

public class susdegree {

    public static void main(String[] args) {
        
        String filenameParametres = "parameters.csv";
        
        String filenameOchiai      = "Ochiai.csv";
        String filenameTarantula   = "Tarantula.csv";
        String filenameWSR         = "WSR.csv";
        String filenameEWSR        = "EWSR.csv";
        
        try (
            BufferedReader lecteurFichier = new BufferedReader(new FileReader(filenameParametres));
            PrintWriter ecrivainOchiai    = new PrintWriter(filenameOchiai);
            PrintWriter ecrivainTarantula = new PrintWriter(filenameTarantula);
            PrintWriter ecrivainWSR       = new PrintWriter(filenameWSR);
            PrintWriter ecrivainEWSR      = new PrintWriter(filenameEWSR);
        ) {
            String ligne;
            
            while ((ligne = lecteurFichier.readLine()) != null) {
                String[] morceaux = ligne.split(",");
                if (morceaux.length < 4) {
                    //kch error wla
                    continue;
                }
                
                int yanisEp = Integer.parseInt(morceaux[0].trim());
                int yanisEf = Integer.parseInt(morceaux[1].trim());
                int douaaNp      = Integer.parseInt(morceaux[2].trim());
                int douaaNf      = Integer.parseInt(morceaux[3].trim());
                
                
                double ep = (double) yanisEp;
                double ef = (double) yanisEf;
                double np = (double) douaaNp;
                double nf = (double) douaaNf;
                
                
                // Ochiai = ef / sqrt((ef + nf)*(ef + ep))
                double ochiaiValeur = 0.0;
                double denomOchiai = (ef + nf) * (ef + ep);
                if (denomOchiai > 0) {
                    ochiaiValeur = ef / Math.sqrt(denomOchiai);
                }
                
                // Tarantula = [ef/(ef+nf)] / ([ef/(ef+nf)] + [ep/(ep+np)])
                double tarantulaValeur = 0.0;
                double failRatio = 0.0;
                double passRatio = 0.0;
                
                if ((ef + nf) > 0) {
                    failRatio = ef / (ef + nf);
                }
                if ((ep + np) > 0) {
                    passRatio = ep / (ep + np);
                }
                
                double denomTarantula = failRatio + passRatio;
                if (denomTarantula > 0) {
                    tarantulaValeur = failRatio / denomTarantula;
                }
                
                // WSR = (ef/(ef + nf)) * (ep/(ep + np))
                double wsrValeur = failRatio * passRatio;
                
                
                // EWSR = WSR * Ochiai
                double ewsrValeur = wsrValeur * ochiaiValeur;
                
                
                ecrivainOchiai.println(ochiaiValeur);
                ecrivainTarantula.println(tarantulaValeur);
                ecrivainWSR.println(wsrValeur);
                ecrivainEWSR.println(ewsrValeur);
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
