package DAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {

    int WRONG_ID = -1;

     public void closeConnection(Connection c){
        if(c!= null){
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public int getID(PreparedStatement st) throws SQLException {
        ResultSet set = st.getGeneratedKeys();
        if(set.next()) {
            return set.getInt(1);
        }

        return WRONG_ID;
    }
    public byte[] getHash(String password) {
         byte[] passwordBytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            MessageDigest mdc = (MessageDigest) md.clone();
            passwordBytes = mdc.digest(password.getBytes());
        } catch (CloneNotSupportedException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordBytes;
    }
}
