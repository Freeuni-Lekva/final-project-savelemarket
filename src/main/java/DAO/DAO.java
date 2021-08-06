package DAO;

import java.sql.Connection;
import java.sql.SQLException;

public class DAO {
     public void closeConnection(Connection c){
        if(c!= null){
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
