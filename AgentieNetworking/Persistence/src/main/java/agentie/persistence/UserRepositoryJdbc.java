package agentie.persistence;

import agentie.UserRepository;
import agentie.model.Agent;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UserRepositoryJdbc implements UserRepository {
    private JdbcUtils jdbcUtils;
    public UserRepositoryJdbc(Properties jdbcProps){
        jdbcUtils=new JdbcUtils(jdbcProps);
    }




    @Override
    public Agent findBy(String username) {
        return null;
    }

    @Override
    public Agent findBy(String username, String pass) {
        System.out.println("JDBC findBy 2 params");
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select name from users where username=? and password=?")){
            preStmt.setString(1,username);
            preStmt.setString(2,pass);
            ResultSet result=preStmt.executeQuery();
            boolean resOk=result.next();
            System.out.println("findBy user, pass "+resOk);
            if (resOk) {
                Agent user=new Agent(username);
                user.setNume(result.getString("name"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    /* @Override
        public Iterable<Agent> getFriendsOf(Agent user) {
            System.out.println("JDBC get friends of");
            Connection con=jdbcUtils.getConnection();
            ArrayList<Agent> result=new ArrayList<Agent>();
            try (PreparedStatement preStmt=con.prepareStatement("select friends.userA, friends.userB  from friends where  friends.userA=? or friends.userB=?")){
                 preStmt.setString(1,user.getId());
                preStmt.setString(2,user.getId());
                ResultSet rs=preStmt.executeQuery();
                while(rs.next()){
                    String friendA=rs.getString(1);
                    String friendB=rs.getString(2);
                    User u=new User(friendA.equals(user.getId())?friendB:friendA);
                    result.add(u);
                }

            } catch (SQLException e) {
                System.out.println("Error DB "+e);
            }
            return result;
        }
    */
    @Override
    public void save(Agent user) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public Agent findOne(String s) {
        return null;
    }

    @Override
    public void update(String s, Agent user) {

    }

    @Override
    public List<Agent> findAll() {
        return null;
    }
}
