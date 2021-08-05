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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO shop_store (price, location_id, writer_mail, create_time) " +
                    "VALUES (?,?,?,?);");
            statement.setDouble(1, shoppingItem.getPrice());
            LocationStore locStore = new LocationStoreDao(dataSource);
            statement.setInt(2, locStore.getLocationId(shoppingItem.getDesiredLocation().getName(),
                    shoppingItem.getDesiredLocation().getSessionNumber()));
            statement.setString(3, shoppingItem.getWriterAccount().getMail());
            statement.setString(4,getCurrentTime());
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
    public void removeItem(int shopItemId) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM shop_store WHERE shop_item_id = ?");
            statement.setInt(1, shopItemId);
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
                Location desiredLocation = getDesLocation(conn, itemId);
                double price = rs.getDouble(2);
                String time = rs.getString(3);
                SaveleShoppingItem shoppingItem = new SaveleShoppingItem(itemId,time, writerAccount, desiredLocation, price);
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
                Location desiredLocation = getDesLocation(conn, itemId);
                double price = rs.getDouble(2);
                String time = rs.getString(3);
                SaveleShoppingItem shoppingItem = new SaveleShoppingItem(itemId,time, writerAccount, desiredLocation, price);                ret.add(shoppingItem);
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


    private Location getDesLocation(Connection connection, int itemId){
        Location result = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT location_name, sess FROM shop_store INNER JOIN" +
                    " locations USING (location_id) WHERE shop_item_id = ?;");
            statement.setInt(1, itemId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                result = new SaveleLocation(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    private Account getWriterAccount(Connection connection, int itemId){
        Account result = null;
        try {
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    private String getCurrentTime(){
        java.util.Date dt = new java.util.Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }
}
