package agentie.network.dto;

import java.io.Serializable;

public class BiletDTO implements Serializable{
    private int id;
    private String nume;
    private String turist;
    private String adresa;
    private int locuri;

    public BiletDTO(int id, String nume, String turist, String adresa, int locuri) {
        this.id = id;
        this.nume = nume;
        this.turist = turist;
        this.adresa = adresa;
        this.locuri = locuri;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getTurist() {
        return turist;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getLocuri() {
        return locuri;
    }

    @Override
    public String toString() {
        return "BiletDTO{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", turist='" + turist + '\'' +
                ", adresa='" + adresa + '\'' +
                ", locuri=" + locuri +
                '}';
    }
}
