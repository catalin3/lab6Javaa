package agentie.model;

import java.io.Serializable;
import java.util.Objects;

public class Zbor implements Serializable{
    private int id;
    private String destiantie;
    private String plecare;
    private int nrLocuri;
    private String aeroport;

    public Zbor() {
    }

    public Zbor(int id, String destiantie, String plecare, String aeroport, int nrLocuri) {
        this.id = id;
        this.destiantie = destiantie;
        this.plecare = plecare;
        this.nrLocuri = nrLocuri;
        this.aeroport = aeroport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestinatie() {
        return destiantie;
    }

    public void setDestiantie(String destiantie) {
        this.destiantie = destiantie;
    }

    public String getPlecare() {
        return plecare;
    }

    public void setPlecare(String plecare) {
        this.plecare = plecare;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    public String getAeroport() {
        return aeroport;
    }

    public void setAeroport(String aeroport) {
        this.aeroport = aeroport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zbor zbor = (Zbor) o;
        return id == zbor.id &&
                nrLocuri == zbor.nrLocuri &&
                Objects.equals(destiantie, zbor.destiantie) &&
                Objects.equals(plecare, zbor.plecare) &&
                Objects.equals(aeroport, zbor.aeroport);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, destiantie, plecare, nrLocuri, aeroport);
    }
}
