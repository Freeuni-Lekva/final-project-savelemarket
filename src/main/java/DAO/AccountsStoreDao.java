package DAO;

import model.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountsStoreDao extends DAO implements AccountsStore {
    private DataSource dataSource;

    public AccountsStoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addAccount(Account account) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("" +
                            "INSERT INTO accounts (first_name, last_name, mail, location_id, pass) " +
                            "VALUES (?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            LocationStore locStore = new LocationStoreDao(dataSource);
            int locId = locStore.getLocationId(account.getLocation().getName(), account.getLocation().getSessionNumber());
            statement.setString(1, account.getName());
            statement.setString(2, account.getLastName());
            statement.setString(3, account.getMail());
            statement.setInt(4, locId);
            statement.setBytes(5, account.getPasswordHash());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void removeAccount(Account account) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts WHERE mail = ?");
            statement.setString(1, account.getMail());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updateLocation(Account account, Location location) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET location_id = ? WHERE mail = ?");
            LocationStore locStore = new LocationStoreDao(dataSource);
            int locId = locStore.getLocationId(location.getName(), location.getSessionNumber());
            statement.setInt(1, locId);
            statement.setString(2, account.getMail());
            statement.executeUpdate();
            account.setLocation(location);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Returns true if accounts store data table contains
     * account with private key 'mail'. Otherwise returns false.
     *
     * @param mail
     * @return
     */
    @Override
    public boolean containsAccount(String mail) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT first_name, last_name, mail, location_id, pass FROM accounts WHERE mail = ?;");
            statement.setString(1, mail);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return true;
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return false;
    }

    @Override
    public Account getAccount(String mail) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT first_name, last_name, mail, location_id, pass FROM accounts WHERE mail = ?");
            statement.setString(1, mail);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String gmail = rs.getString(3);
                int locationId = rs.getInt(4);
                byte[] password = rs.getBytes(5);
                LocationStore locationStore = new LocationStoreDao(dataSource);
                Location loc = locationStore.getLocationById(locationId);
                return new StudentAccount(firstName, lastName, password, gmail,loc);
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    /**
     * Reads all fields from AccountStore database table,
     * turns them into appropriate account objects
     * and returns their list;
     * Useful for testing and showing data.
     *
     * @return List<Account>
     */
    @Override
    public List<Account> getAllAccounts() {
        Connection conn = null;
        List<Account> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT first_name, last_name, mail, location_id, pass FROM accounts");
            while (rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String mail = rs.getString(3);
                int locationId = rs.getInt(4);
                byte[] password = rs.getBytes(5);
                LocationStore locationStore = new LocationStoreDao(dataSource);
                ret.add(new StudentAccount(firstName, lastName, password, mail, locationStore.getLocationById(locationId)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return ret;
    }

 /** Returns Account class object read from database and suitable for the given mail*/
    private Account getAccountByMail(Connection connection, String mail){
        Account result = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT first_name, last_name, mail, location_id, pass FROM accounts WHERE mail = ?");
            statement.setString(1, mail);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                String name = rs.getString(1);
                String lastName = rs.getString(2);
                int locId = rs.getInt(4);
                byte[] pass = rs.getBytes(5);
                LocationStore locStore = new LocationStoreDao(dataSource);
                result = new StudentAccount(name, lastName, pass, mail, locStore.getLocationById( locId));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
    }
        return result;
    }
}

