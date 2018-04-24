package agentie.persistence;


import agentie.ZborRepository;
import agentie.model.Zbor;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ZborJdbcRepository implements ZborRepository<Integer, Zbor> {
    private JdbcUtils dbUtils;

    public ZborJdbcRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public int size() {
        Connection con=dbUtils.getConnection();
        try
        {
            PreparedStatement preStmt = con.prepareStatement("SELECT COUNT (*) as SIZE from Zbor");
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
    public void save(Zbor entity) {
        Connection con =dbUtils.getConnection();
        String s=entity.getPlecare();
        Timestamp date = Timestamp.valueOf(entity.getPlecare());
        try(PreparedStatement preStmt=con.prepareStatement("insert into Zbor VALUES (?,?,?,?,?)")) {
        preStmt.setInt(1,entity.getId());
        preStmt.setString(2,entity.getDestinatie());
        preStmt.setString(3,  entity.getPlecare());
        preStmt.setString(4,entity.getAeroport());
        preStmt.setInt(5,entity.getNrLocuri());
        preStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Eror DB" + e);
        } ;

    }

    @Override
    public void delete(Integer integer) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Zbor where IdBilet=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Eror DB" + e);
        }
    }

        @Override
    public void update(Integer integer, Zbor entity) {
            Connection con=dbUtils.getConnection();
            String s=entity.getPlecare();
          //  Timestamp date = Timestamp.valueOf(entity.getPlecare());
            try(PreparedStatement preStmt=con.prepareStatement("update Zbor set LocuriDisponibile=? where IdBilet=?")){
               // preStmt.setString(2,entity.getDestinatie());
               // preStmt.setTimestamp(3,  date);
               // preStmt.setString(4,entity.getAeroport());
                preStmt.setInt(1,entity.getNrLocuri());
                preStmt.setInt(2,entity.getId());
                preStmt.executeUpdate();
            }catch(SQLException ex){
                System.out.println("Error DB "+ex);
            }
    }

    @Override
    public Zbor findOne(Integer integer) {
        Connection con=dbUtils.getConnection();
        try (PreparedStatement preStmt =con.prepareStatement("select * from Zbor where IdBilet=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()){
                if(result.next()){
                    int id=result.getInt("idBilet");
                    String destinatie=result.getString("Destinatie");
                    String plecare=result.getString("DataPlecari");
                    String aeroport=result.getString("Aeroport");
                    int nrLocuri=result.getInt("LocuriDisponibile");
                    String s=String.valueOf(plecare);
                    Zbor zb=new Zbor(id,destinatie,plecare, aeroport, nrLocuri);
                    return zb;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return null;
    }

    @Override
    public List<Zbor> findAll() {
        Connection con=dbUtils.getConnection();
        List<Zbor> zbors=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("SELECT * from Zbor")){
            try(ResultSet result=preStmt.executeQuery()){
                while (result.next()) {
                    int id = result.getInt("idBilet");
                    String destinatie = result.getString("Destinatie");
                    String plecare = result.getString("DataPlecari");
                    String aeroport = result.getString("Aeroport");
                    int nrLocuri = result.getInt("LocuriDisponibile");
                    String s = String.valueOf(plecare);
                    Zbor zb = new Zbor(id, destinatie, s, aeroport, nrLocuri);
                    zbors.add(zb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zbors;
    }

    @Override
    public List<Zbor> filterBy(String dataPlecarii) {
        return null;
    }

    @Override
    public List<Zbor> filetrBy(String destinatie,String plecare) {
        Connection con=dbUtils.getConnection();
        List<Zbor> zbors=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("SELECT * from Zbor WHERE Destinatie=? and DataPlecari=? ")){
            preStmt.setString(1,destinatie);
            preStmt.setString(2,plecare);
            try(ResultSet result=preStmt.executeQuery()){
                while (result.next()) {
                    int id = result.getInt("idBilet");
                    String destinaties = result.getString("Destinatie");
                    String plecares = result.getString("DataPlecari");
                    String aeroport = result.getString("Aeroport");
                    int nrLocuri = result.getInt("LocuriDisponibile");
                   // String s = String.valueOf(plecare);
                    Zbor zb = new Zbor(id, destinaties, plecares, aeroport, nrLocuri);
                    zbors.add(zb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zbors;
    }
}
