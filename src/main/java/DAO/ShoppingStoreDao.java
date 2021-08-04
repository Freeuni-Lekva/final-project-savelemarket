package DAO;

import model.ShoppingItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO shop_store (price, location_name, location_sess_num, writer_mail) " +
                    "VALUES (?,?,?,?);");
            statement.setDouble(1, shoppingItem.getPrice());
            statement.setString(2, shoppingItem.getDesiredLocation().getName());
            statement.setInt(3, shoppingItem.getDesiredLocation().getSessionNumber());
            statement.setString(4, shoppingItem.getWriterAccount().getMail());
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

    }

    @Override
    public List<ShoppingItem> getAllItems() {
        return null;
    }
}
