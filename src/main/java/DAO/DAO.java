package DAO;

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

}
