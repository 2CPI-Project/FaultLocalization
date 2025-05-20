package application;

import javafx.beans.property.SimpleIntegerProperty;

public class ParameterEntry {
    private final SimpleIntegerProperty ep;
    private final SimpleIntegerProperty ef;
    private final SimpleIntegerProperty np;
    private final SimpleIntegerProperty nf;

    public ParameterEntry(int ep, int ef, int np, int nf) {
        this.ep = new SimpleIntegerProperty(ep);
        this.ef = new SimpleIntegerProperty(ef);
        this.np = new SimpleIntegerProperty(np);
        this.nf = new SimpleIntegerProperty(nf);
    }

    // Getters and property methods
    public int getEp() { return ep.get(); }
    public int getEf() { return ef.get(); }
    public int getNp() { return np.get(); }
    public int getNf() { return nf.get(); }

    public SimpleIntegerProperty epProperty() { return ep; }
    public SimpleIntegerProperty efProperty() { return ef; }
    public SimpleIntegerProperty npProperty() { return np; }
    public SimpleIntegerProperty nfProperty() { return nf; }
}