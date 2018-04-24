package agentie.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties jdbcProp;

    public JdbcUtils(Properties props){
        jdbcProp=props;
    }

    private Connection instance=null;
    private Connection getNewConncetion(){
        String driver=jdbcProp.getProperty("chat.jdbc.driver");
        String url=jdbcProp.getProperty("chat.jdbc.url");
        String user=jdbcProp.getProperty("chat.jdbc.user");
        String pass=jdbcProp.getProperty("chat.jdbc.pass");
        Connection con=null;
        try{
            Class.forName("org.sqlite.JDBC");
            if(user!=null&&pass!=null)
                con= DriverManager.getConnection(url,user,pass);
            else
                con=DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    public Connection getConnection(){
        try{
            if(instance==null || instance.isClosed())
                instance=getNewConncetion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
