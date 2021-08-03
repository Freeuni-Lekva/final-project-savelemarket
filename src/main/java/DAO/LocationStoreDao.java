package DAO;

import model.Account;
import model.Location;
import model.SaveleLocation;
import model.StudentAccount;

import javax.sql.DataSource;
import javax.ws.rs.NotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationStoreDao implements LocationStore{
    private DataSource dataSource;

    public LocationStoreDao(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public Location getLocation(Account account) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select location_name, sess from accounts inner join locations using (location_id) where mail=?;");
            statement.setString(1, account.getMail());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return new SaveleLocation(rs.getString(1), rs.getInt(2));
            }
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

        return null;
    }

    @Override
    public List<Location> getAllLocations() {
        List<Location> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select location_name, sess from locations");
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                result.add(new SaveleLocation(rs.getString(1), rs.getInt(2)));
            }
            return result;
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
        return null;
    }

    @Override
    public List<Account> getStudents(Location location) {
        Connection connection = null;
        List<Account> result = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from accounts inner join locations using (location_id) where location_name=? and sess =?;");
            statement.setString(1, location.getName());
            statement.setInt(2, location.getSessionNumber());
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                result.add(new StudentAccount(rs.getString(2), rs.getString(3),
                        rs.getString(5), rs.getString(4),
                        new SaveleLocation(rs.getString(6), rs.getInt(7))));
            }
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public List<String> getUniqueLocations() {
        Connection connection = null;
        List<String> result = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select distinct location_name from locations");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void addLocation(Location location, ChatStore chatStore) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            // probably needs to check for duplicates (location shouldn't exist in database)
            int chatID = chatStore.createPublicChat(); // creates empty public chat
            location.setChatID(chatID);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO locations (location_name, sess, chat_id) " +
                    "VALUES (?,?,?);");
            statement.setString(1, location.getName());
            statement.setInt(2, location.getSessionNumber());
            statement.setInt(3,chatID);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
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
    public boolean hasLocation(String locationName, int sessionNumber) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select location_name, sess from locations where location_name = ? and sess = ?");
            statement.setString(1, locationName);
            statement.setInt(2, sessionNumber);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

}
