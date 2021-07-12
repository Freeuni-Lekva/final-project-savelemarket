package DAO;

import model.Account;
import model.Location;
import model.SaveleLocation;
import model.StudentAccount;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountsStoreDao implements AccountsStore {
    private DataSource dataSource;

    public AccountsStoreDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void addAccount(Account account) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("" +
                            "INSERT INTO accounts (first_name, last_name, mail, location_id, pass) " +
                            "VALUES (?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            int locId = getLocationId(connection, account);
            statement.setString(1, account.getName());
            statement.setString(2, account.getLastName());
            statement.setString(3, account.getMail());
            statement.setInt(4, locId);
            statement.setBytes(5, account.getPasswordHash());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public void removeAccount(Account account) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM accounts WHERE mail = ?");
            statement.setString(1, account.getMail());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateLocation(Account account, Location location) {

    }

    @Override
    public boolean hasAccount(String mail) {
//        Connection conn = null;
//        try {
//            conn = dataSource.getConnection();
//            Statement stm = conn.createStatement();
//            ResultSet rs = stm.executeQuery(
//                    "SELECT first_name, last_name, mail, location_id, pass FROM accounts WHERE mail = "+mail);
//            if(rs.next()) return true;
//            return false;
//        } catch (SQLException throwables) { throwables.printStackTrace();
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            }
//        }
        return false;
    }

    @Override
    public List<Account> getAllAccounts() {
        Connection conn = null;
        List<Account> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
                    "SELECT first_name, last_name, mail, location_id, pass FROM accounts");
            while (rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String mail = rs.getString(3);
                int locationId = rs.getInt(4);
                byte[] password = rs.getBytes(5);
                ret.add(new StudentAccount(firstName,lastName,password, mail,getLocationById(conn, locationId)));
            }
        } catch (SQLException throwables) { throwables.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return ret;
    }


    /**
     * Reads all fields from DataBase associated with location ID, converts them
     * to location object and returns it.
     */
    private Location getLocationById(Connection connection, int locationId){
        Location result = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT location_name, sess FROM locations WHERE location_id = ?");
            statement.setInt(1, locationId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                result = new SaveleLocation(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }


    /** Returns primary key of the account's location form Data Base. */
    private int getLocationId(Connection conn, Account account){
        PreparedStatement statement = null;
        try {
            String locationName = account.getLocation().getName();
            int sessionNum = account.getLocation().getSessionNumber();
            statement = conn.prepareStatement("SELECT location_id FROM locations WHERE  sess = ? AND location_name = ?;");
            statement.setInt(1, sessionNum);
            statement.setString(2, locationName);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }
}
