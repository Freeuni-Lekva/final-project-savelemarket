package DAO;

import model.*;

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
    public Location getLocation(String mail) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select location_name, sess, chat_id from accounts inner join locations using (location_id) where mail=?;");
            statement.setString(1, mail);
            ResultSet rs = statement.executeQuery();
            ChatStore chatStore = new ChatStoreDao(dataSource);
            if(rs.next()){
                Chat ch = chatStore.getPublicChat(rs.getInt("chat_id"));
                return new SaveleLocation(rs.getString(1), rs.getInt(2),ch);
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
    public int addLocation(Location location, ChatStore chatStore) {
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
            return chatID;
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
        return -1;
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



    @Override
    public Location getLocation(String locationName, int locationSession) {
        Location loc = null;
        try{
            PreparedStatement st = dataSource.getConnection().prepareStatement("SELECT location_name,sess,chat_id,location_id FROM locations WHERE location_name = ? AND sess = ?");
            st.setString(1,locationName);
            st.setInt(2,locationSession);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                loc = new SaveleLocation(rs.getString("location_name"),rs.getInt("sess"),rs.getInt("chat_id"));
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return loc;
    }

    @Override
    /////////// added 2 static methods /////

    /** Returns primary key of the location from Data Base suitable for the given
     *  location name and session number.
     */
    public int getLocationId(String locationName, int sessionNum){
        PreparedStatement statement = null;
        try {
            Connection conn = dataSource.getConnection();
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

    @Override
    public Location getLocationById (int locationId){
        Location result = null;
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement("SELECT location_name, sess, chat_id FROM locations WHERE location_id = ?");
            statement.setInt(1, locationId);
            ResultSet rs = statement.executeQuery();
            ChatStore chatStore = new ChatStoreDao(dataSource);
            if (rs.next()) {
                Chat ch = chatStore.getPublicChat(rs.getInt("chat_id"));
                result = new SaveleLocation(rs.getString(1), rs.getInt(2), ch); // needs to get chat too
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Location> getPossibleLocations(String locationName, int sessinNum) {
        Connection conn = null;
        List<Location> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement statement;
            if(locationName == null && sessinNum == SaveleLocation.NO_OP_SESS){
                statement = conn.prepareStatement("SELECT location_name, sess, chat_id FROM locations");
            }else if(locationName == null && sessinNum != SaveleLocation.NO_OP_SESS){
                statement = conn.prepareStatement("SELECT location_name, sess, chat_id FROM locations WHERE sess = ?");
                statement.setInt(1, sessinNum);
            }else if(locationName != null && sessinNum == SaveleLocation.NO_OP_SESS){
                statement = conn.prepareStatement("SELECT location_name, sess, chat_id FROM locations WHERE location_name = ?");
                statement.setString(1, locationName);
            }else {
                statement = conn.prepareStatement("SELECT location_name, sess, chat_id FROM locations WHERE location_name = ? AND sess = ?;");
                statement.setString(1, locationName);
                statement.setInt(2, sessinNum);
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String locName = rs.getString(1);
                int sessNum = rs.getInt(2);
                int chatId = rs.getInt(3);
                Location newLocation = new SaveleLocation(locName, sessNum, chatId);
                ret.add(newLocation);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

}

