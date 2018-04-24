package agentie.persistence;

import agentie.BiletRepository;
import agentie.model.Agent;
import agentie.model.Bilet;
import agentie.model.Zbor;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class BiletJdbcRepository implements BiletRepository<Integer,Bilet>{
    private JdbcUtils dbUtils;

    public BiletJdbcRepository(Properties dbUtils) {
        this.dbUtils = new JdbcUtils(dbUtils);
    }

    @Override
    public int size() {
        Connection con=dbUtils.getConnection();
        try
        {
            PreparedStatement preStmt = con.prepareStatement("SELECT COUNT (*) as SIZE from Bilet");
            ResultSet resultSet = preStmt.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("SIZE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DB"+e);
        }

        return 0;
    }

    @Override
    public void save(Bilet entity) {
        Connection con =dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("insert into Bilet (idBilet,Nume,NumeTuristi,Adresa,NrLocuri) VALUES (?,?,?,?,?)")) {
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setString(3,  entity.getTurist());
            preStmt.setString(4,entity.getAdresa());
            preStmt.setInt(5,entity.getLocuri());
            preStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Eror DB" + e);
        } ;

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Bilet entity) {

    }

    @Override
    public Bilet findOne(Integer integer) {
        Connection con=dbUtils.getConnection();
        try (PreparedStatement preStmt =con.prepareStatement("select * from Bilet where IdBilet=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()){
                if(result.next()){
                    int id=result.getInt("idBilet");
                    String nume=result.getString("Nume");
                    String turist=result.getString("NumeTuristi");
                    String adresa=result.getString("Adresa");
                    int nrLocuri=result.getInt("NrLocuri");

                    Bilet zb=new Bilet(id,nume,turist, adresa, nrLocuri);
                    return zb;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return null;
    }

    @Override
    public List<Bilet> findAll() {
        return null;
    }

    @Override
    public Iterable<Bilet> findAllTurist(String n) {
        return null;
    }
}
