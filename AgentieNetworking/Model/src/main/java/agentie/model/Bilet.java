package agentie.model;

import java.io.Serializable;

public class Bilet implements Serializable{
    private int id;
    private String nume;
    private String turist;
    private String adresa;
    private int locuri;

    public Bilet() {
    }

    public Bilet(int id, String nume, String turist, String adresa, int locuri) {
        this.id = id;
        this.nume = nume;
        this.turist = turist;
        this.adresa = adresa;
        this.locuri = locuri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getTurist() {
        return turist;
    }

    public void setTurist(String turist) {
        this.turist = turist;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getLocuri() {
        return locuri;
    }

    public void setLocuri(int locuri) {
        this.locuri = locuri;
    }
}
