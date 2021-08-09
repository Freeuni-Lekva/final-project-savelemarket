package DAO;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
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
        addAdminAccount(ds,"admin","admin");

    }
    private static void addAdminAccount(DataSource ds, String username, String password){
        Connection connection = null;
        try {
            connection = ds.getConnection();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            MessageDigest mdc = (MessageDigest) md.clone();
            byte[] passwordBytes = mdc.digest(password.getBytes());
            PreparedStatement st = connection.prepareStatement("INSERT INTO admins(username, password) VALUES(?,?);");
            st.setString(1,username);
            st.setBytes(2,passwordBytes);
            st.executeUpdate();
        } catch (NoSuchAlgorithmException | CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
        }

    }
}
