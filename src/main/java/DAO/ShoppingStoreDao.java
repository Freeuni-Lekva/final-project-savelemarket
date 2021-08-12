package DAO;

import model.*;
import org.w3c.dom.ls.LSOutput;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingStoreDao extends DAO implements ShoppingStore {
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
                    "VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
            statement.setDouble(1, shoppingItem.getPrice());
            statement.setString(2, shoppingItem.getWriterAccount().getMail());
            statement.setString(3, getCurrentTime());
            int res = statement.executeUpdate();
            int shopItemId = getID(statement);
            LocationStore locationStore = new LocationStoreDao(dataSource);
            List<Location> locs = shoppingItem.getDesiredLocations();
            PreparedStatement addStatement = connection.prepareStatement("INSERT INTO shop_locations (shop_item_id, location_id)" +
                    "VALUES (?,?);");
            addStatement.setInt(1, shopItemId);
            for(Location l : locs){
                int locId = locationStore.getLocationId(l.getName(), l.getSessionNumber());
                addStatement.setInt(2, locId);
                addStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void removeItem(int shopItemId) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement2 =
                    connection.prepareStatement("DELETE FROM shop_locations WHERE shop_item_id = ?;");
            statement2.setInt(1, shopItemId);
            statement2.executeUpdate();
            PreparedStatement statement1 =
                    connection.prepareStatement("DELETE FROM shop_store WHERE shop_item_id = ?;");
            statement1.setInt(1, shopItemId);
            statement1.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public int getItemId(String writerMail, String createTime){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT shop_item_id FROM shop_store " +
                    "WHERE writer_mail = ? AND create_time = ?;");
            statement.setString(1, writerMail);
            statement.setString(2, createTime);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return -1;
    }


    @Override
    public void removeAllItemFor(String accountMail) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement removeFromShopLocations = connection.prepareStatement("DELETE sl FROM shop_store AS ss JOIN shop_locations AS sl ON ss.shop_item_id = sl.shop_item_id" +
                    " WHERE ss.writer_mail = ?;");
            PreparedStatement  removeFromShopStore = connection.prepareStatement("DELETE from shop_store WHERE writer_mail = ?;");
            removeFromShopLocations.setString(1, accountMail);
            removeFromShopLocations.executeUpdate();
            removeFromShopStore.setString(1, accountMail);
            removeFromShopStore.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
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
                ret.add(getShoppingItemFromResultSet(rs, conn));
            }
        } catch (SQLException throwables) { throwables.printStackTrace();
        } finally {
            closeConnection(conn);
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
                ret.add(getShoppingItemFromResultSet(rs, conn));
            }
        } catch (SQLException throwables) { throwables.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return ret;
    }

    
    @Override
    public ShoppingItem getItemById(int id){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT shop_item_id, price, create_time FROM shop_store " +
                    "WHERE shop_item_id = ?;");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if(rs.next()) {
                return getShoppingItemFromResultSet(rs, conn);
            }
        } catch (SQLException throwables) { throwables.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return null;
    }


    /**
     * If price < 0 you want to sell something and get items in which writer
     * buys location/locations, otherwise you want to buy item.
     */
    @Override
    public List<ShoppingItem> getFilteredItems(String location_name, int sess,  boolean wantToBuy, double price) {
        Connection connection = null;
        List<ShoppingItem> shoppingItems = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(getAppropriateSqlCommand(location_name, sess, wantToBuy));
            setFieldsToStatement(statement, location_name, sess, price);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int itemId = rs.getInt("ss.shop_item_id");
                shoppingItems.add(getItemById(itemId));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return shoppingItems;
    }


    private String getAppropriateSqlCommand(String location_name, int sess,  boolean wantToBuy){
        StringBuilder stringBuilder = new StringBuilder();
        if(wantToBuy){
            stringBuilder.append("SELECT DISTINCT ss.shop_item_id FROM shop_store ss INNER JOIN accounts a " +
                    "ON (ss.writer_mail = a.mail) INNER JOIN locations l ON (a.location_id = l.location_id) WHERE");
        }else{
            stringBuilder.append("SELECT DISTINCT ss.shop_item_id FROM shop_locations ss " +
                    "INNER JOIN locations l ON (ss.location_id = l.location_id) " +
                    "INNER JOIN shop_store sl ON (ss.shop_item_id = sl.shop_item_id) WHERE");
        }
        if(location_name == null && sess != -1){ stringBuilder.append(" l.sess = ? AND ");}
        if(location_name != null && sess == -1){ stringBuilder.append(" l.location_name = ? AND ");}
        if(location_name != null && sess != -1){ stringBuilder.append(" l.location_name = ? AND l.sess = ? AND ");}
        if(wantToBuy) {
            stringBuilder.append(" ss.price <= ?;");
        }else{
            stringBuilder.append(" sl.price <= ?;");
        }
        return stringBuilder.toString();
    }

    private void setFieldsToStatement(PreparedStatement statement, String location_name, int sess, double price) throws SQLException {
        if(location_name == null && sess == -1){
            statement.setDouble(1, price);
        } else if(location_name != null && sess == -1){
            statement.setString(1, location_name);
            statement.setDouble(2, price);
        } else if(location_name == null && sess != -1){
            statement.setInt(1, sess);
            statement.setDouble(2, price);
        } else{
            statement.setString(1, location_name);
            statement.setInt(2, sess);
            statement.setDouble(3, price);
        }
    }

    private ShoppingItem getShoppingItemFromResultSet(ResultSet rs, Connection conn) throws SQLException {
        int itemId = rs.getInt(1);
        Account writerAccount = getWriterAccount(conn, itemId);
        List<Location> desiredLocations = getLocationsFor(conn, itemId);
        double price = rs.getDouble(2);
        String time = rs.getString(3);
        return new SaveleShoppingItem(itemId,time, writerAccount, desiredLocations, price);
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
        PreparedStatement stm = connection.prepareStatement("SELECT location_name, sess, chat_id FROM shop_locations INNER JOIN " +
                "locations USING (location_id) WHERE shop_item_id = ?;");
        stm.setInt(1, shopItId);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            String locationName = rs.getString(1);
            int sessionNum = rs.getInt(2);
            int chatId = rs.getInt(3);
            Location loc = new SaveleLocation(locationName,sessionNum,chatId);
            ret.add(loc);
        }
        return ret;
    }

    private String getCurrentTime(){
        java.util.Date dt = new java.util.Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }

}
