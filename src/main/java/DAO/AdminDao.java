package DAO;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import model.Message;

import javax.sql.DataSource;
import java.lang.ref.PhantomReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDao extends DAO {

    public static void main(String[] args) {
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("myDatabase");
        ds.setUser("root");
        ds.setPassword("");
        AdminDao dao = new AdminDao();
        dao.addAdminAccount(ds,"admin","admin");

    }
    private void addAdminAccount(DataSource ds, String username, String password){
        Connection connection = null;
        try {
            connection = ds.getConnection();
            byte[] passwordBytes = getHash(password);
            PreparedStatement st = connection.prepareStatement("INSERT INTO admins(username, password) VALUES(?,?);");
            st.setString(1,username);
            st.setBytes(2,passwordBytes);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            closeConnection(connection);
        }

    }
    private void removeAdminAccount(DataSource ds, String username){
        Connection connection = null;
        try{
            connection = ds.getConnection();
            PreparedStatement st = connection.prepareStatement("DELETE FROM admins WHERE username = ?;");
            st.setString(1,username);
            st.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
}
