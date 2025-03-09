import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;

public class susdegree_approxi {

    public static void main(String[] args) {
        
        String filenameParametres = "parametres.csv";
        
        String filenameOchiai      = "Ochiai.csv";
        String filenameTarantula   = "Tarantula.csv";
        String filenameJaccard     = "Jaccard.csv";
        String filenameZoltar      = "Zoltar.csv";
        
        try (
            BufferedReader lecteurFichier = new BufferedReader(new FileReader(filenameParametres));
            PrintWriter ecrivainOchiai    = new PrintWriter(filenameOchiai);
            PrintWriter ecrivainTarantula = new PrintWriter(filenameTarantula);
            PrintWriter ecrivainJaccard   = new PrintWriter(filenameJaccard);
            PrintWriter ecrivainZoltar    = new PrintWriter(filenameZoltar);
        ) {
            String ligne;
            
            while ((ligne = lecteurFichier.readLine()) != null) {
                String[] morceaux = ligne.split(",");
                if (morceaux.length < 4) {
                    continue; // Skip invalid lines
                }
                
                int yanisEp = Integer.parseInt(morceaux[0].trim());
                int yanisEf = Integer.parseInt(morceaux[1].trim());
                int douaaNp = Integer.parseInt(morceaux[2].trim());
                int douaaNf = Integer.parseInt(morceaux[3].trim());
                
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
                double failRatio = (ef + nf) > 0 ? ef / (ef + nf) : 0.0;
                double passRatio = (ep + np) > 0 ? ep / (ep + np) : 0.0;
                double denomTarantula = failRatio + passRatio;
                if (denomTarantula > 0) {
                    tarantulaValeur = failRatio / denomTarantula;
                }
                
                // Jaccard = ef / (ef + nf + ep)
                double jaccardValeur = (ef + nf + ep) > 0 ? ef / (ef + nf + ep) : 0.0;
                
                // Zoltar = ef / (ef+nf+ep+((10000*nf*ep)/ef))
                double zoltarValeur = (ef != 0) ? ef / (ef + nf + ep + ((10000.0 * nf * ep) / ef)) : 0.0;
                
                // Write rounded values (one decimal place)
                ecrivainOchiai.println(String.format("%.1f", ochiaiValeur));
                ecrivainTarantula.println(String.format("%.1f", tarantulaValeur));
                ecrivainJaccard.println(String.format("%.1f", jaccardValeur));
                ecrivainZoltar.println(String.format("%.1f", zoltarValeur));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
