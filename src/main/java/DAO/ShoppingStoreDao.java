package DAO;

import model.*;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingStoreDao implements ShoppingStore {
    private final DataSource dataSource;

    public ShoppingStoreDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void addItem(ShoppingItem shoppingItem) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO shop_store (price, writer_mail, create_time) " +
                    "VALUES (?,?,?);");
            statement.setDouble(1, shoppingItem.getPrice());
            statement.setString(2, shoppingItem.getWriterAccount().getMail());
            statement.setString(3,getCurrentTime());
            statement.executeUpdate();
            int shopItemId = getItemId(shoppingItem.getWriterAccount().getMail(), shoppingItem.getCreateTime());
            LocationStore locationStore = new LocationStoreDao(dataSource);
            for(Location l : shoppingItem.getDesiredLocations()){
                PreparedStatement addStatement = connection.prepareStatement("INSERT INTO shop_locations (shop_item_id, location_id)" +
                        "VALUES (?,?)");
                addStatement.setInt(1, shopItemId);
                int locId = locationStore.getLocationId(l.getName(), l.getSessionNumber());
                addStatement.setInt(2, locId);
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
    }

    @Override
    public void removeItem(int shopItemId) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            /// remove from shop_store
            PreparedStatement statement1 =
                    connection.prepareStatement("DELETE FROM shop_store WHERE shop_item_id = ?");
            statement1.setInt(1, shopItemId);
            statement1.executeUpdate();
            /// remove from shop_locations
            PreparedStatement statement2 =
                    connection.prepareStatement("DELETE FROM shop_locations WHERE shop_item_id = ?");
            statement2.setInt(1, shopItemId);
            statement2.executeUpdate();
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
    public void removeAllItemFor(String accountMail) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement getItemsId = connection.prepareStatement("SELECT shop_item_id FROM shop_store WHERE writer_mail = ?");
            getItemsId.setString(1, accountMail);
            ResultSet rs = getItemsId.executeQuery();
            while(rs.next()){
                PreparedStatement  removeFromShopLoc = connection.prepareStatement("DELETE from shop_locations WHERE shop_item_id = ?");
                int id = rs.getInt(1);
                removeFromShopLoc.setInt(1,id);
                removeFromShopLoc.executeUpdate();
            }
            PreparedStatement  removeFromShopstore = connection.prepareStatement("DELETE from shop_store WHERE writer_mail = ?");
            removeFromShopstore.setString(1, accountMail);
            removeFromShopstore.executeUpdate();
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
    public List<ShoppingItem> getAllItemsForAccount(String accountMail) {
        Connection conn = null;
        List<ShoppingItem> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT shop_item_id, price, create_time FROM shop_store WHERE writer_mail = ?;");
            stm.setString(1, accountMail);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt(1);
                Account writerAccount = getWriterAccount(conn, itemId);
                double price = rs.getDouble(2);
                String time = rs.getString(3);
                List<Location> desiredLocations = getLocationsFor(conn, itemId);
                SaveleShoppingItem shoppingItem = new SaveleShoppingItem(itemId,time, writerAccount, desiredLocations, price);
                ret.add(shoppingItem);
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

    @Override
    public List<ShoppingItem> getAllItems() {
        Connection conn = null;
        List<ShoppingItem> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT shop_item_id, price, create_time FROM shop_store;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt(1);
                Account writerAccount = getWriterAccount(conn, itemId);
                List<Location> desiredLocations = getLocationsFor(conn, itemId);
                double price = rs.getDouble(2);
                String time = rs.getString(3);
                SaveleShoppingItem shoppingItem = new SaveleShoppingItem(itemId,time, writerAccount, desiredLocations, price);                ret.add(shoppingItem);
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

    @Override
    public int getItemId(String writerMail, String createDate){
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT shop_item_id FROM shop_store WHERE writer_mail = ? AND create_time = ?;");
            statement.setString(1, writerMail);
            statement.setString(2, createDate);
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

    private Account getWriterAccount(Connection connection, int itemId) throws SQLException {
        Account result = null;
        PreparedStatement statement = connection.prepareStatement("SELECT first_name, last_name, " +
                "mail, pass FROM shop_store s INNER JOIN accounts " +
                "a ON s.writer_mail = a.mail WHERE shop_item_id = ?;");
        statement.setInt(1, itemId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            String name = rs.getString(1);
            String lastName = rs.getString(2);
            String mail = rs.getString(3);
            byte[] pass = rs.getBytes(4);
            LocationStore locStore = new LocationStoreDao(dataSource);
            result = new StudentAccount(name, lastName, pass, mail, locStore.getLocation(mail));
        }
        return result;
    }

    private List<Location> getLocationsFor(Connection connection, int shopItId) throws SQLException {
        List<Location> ret = new ArrayList<>();
        PreparedStatement stm = connection.prepareStatement("SELECT location_name, sess, chatId FROM shop_locations INNER JOIN " +
                "locations USING (location_id) WHERE shop_item_id = ?;");
        stm.setInt(1, shopItId);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            String locationName = rs.getString(1);
            int sessionNum = rs.getInt(2);
            int chatId = rs.getInt(3);
            ret.add(new SaveleLocation(locationName,sessionNum,chatId));
        }
        return ret;
    }

    private String getCurrentTime(){
        java.util.Date dt = new java.util.Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }
}
