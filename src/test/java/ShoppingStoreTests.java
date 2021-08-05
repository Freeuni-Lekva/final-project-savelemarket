import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ShoppingStoreTests {
    private final String serverName = "localhost";
    private final int port = 3306;
    private final String dbName = "myDatabase";
    private final String user = "root";
    private final String password = "";
    private AccountsStoreDao accountsStoreDao;
    private ShoppingStore shoppingStore;

    private SaveleLocation locations[] = {  new SaveleLocation("Kazbegi", 1),
            new SaveleLocation("Kazbegi", 2),
            new SaveleLocation("Kazbegi", 3),
            new SaveleLocation("Tusheti", 1),
            new SaveleLocation("Tusheti", 2),
            new SaveleLocation("Svaneti", 1),
            new SaveleLocation("Marelisi", 1),
            new SaveleLocation("ციმბირი", 2)

    };

    private Account accounts[] = {
            new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem",locations[4]),
            new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]),
            new StudentAccount("Tornike","Totladze","sanebeli123", "ttotl",locations[6]),
            new StudentAccount("Alexa","Inauri","gori1234", "Ainau",locations[3])
    };

    @BeforeEach
    public void init(){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName(serverName);
        ds.setPort(port);
        ds.setDatabaseName(dbName);
        ds.setUser(user);
        ds.setPassword(password);

        initDbs(ds);

        shoppingStore = new ShoppingStoreDao(ds);
    }

    @Test
    public void complexShoppingItemTest(){
        System.out.println("::::: tests :::::");
        ShoppingItem item1 = new SaveleShoppingItem(accounts[0], locations[1], 100);
        shoppingStore.addItem(item1);
        List<ShoppingItem> items1 = shoppingStore.getAllItemsForAccount(accounts[0].getMail());
        for(ShoppingItem sI : items1){
            System.out.println(sI);
            shoppingStore.removeItem(sI.getItemId());
        }
        System.out.println("-------");
        accountsStoreDao.updateLocation(accounts[0], locations[1]);
        Account upDtAcc1 = accountsStoreDao.getAccount(accounts[0].getMail());
        ShoppingItem item2 = new SaveleShoppingItem(upDtAcc1, locations[5], 200);
        shoppingStore.addItem(item2);
        List<ShoppingItem> items2 = shoppingStore.getAllItemsForAccount(upDtAcc1.getMail());
        for(ShoppingItem sI : items2){
            System.out.println(sI);
            shoppingStore.removeItem(sI.getItemId());
        }
        System.out.println("-------");
        accountsStoreDao.updateLocation(upDtAcc1, locations[5]);
        Account upDtAcc2 = accountsStoreDao.getAccount(accounts[0].getMail());
        ShoppingItem item3 = new SaveleShoppingItem(upDtAcc2, locations[7], 400);
        shoppingStore.addItem(item3);
        List<ShoppingItem> items3 = shoppingStore.getAllItemsForAccount(upDtAcc2.getMail());
        for(ShoppingItem sI : items3){
            System.out.println(sI);
            shoppingStore.removeItem(sI.getItemId());
        }
        System.out.println("-------");
    }


    private void initDbs(MysqlConnectionPoolDataSource ds){
        try {
            Connection connection = ds.getConnection();
            PreparedStatement resetShoppingStore = connection.prepareStatement("DROP TABLE IF EXISTS shop_store;");
            resetShoppingStore.executeUpdate();
            PreparedStatement psReset1 = connection.prepareStatement("DROP TABLE IF EXISTS accounts;");
            psReset1.executeUpdate();
            PreparedStatement psReset2 = connection.prepareStatement("DROP TABLE IF EXISTS locations;");
            psReset2.executeUpdate();
            PreparedStatement psInit1 = connection.prepareStatement("CREATE TABLE locations ( " +
                    "`location_name` VARCHAR(64) NOT NULL, " +
                    "`sess` TINYINT NOT NULL, " +
                    "`location_id` INT AUTO_INCREMENT PRIMARY KEY" +
                    ");" +
                    "");
            psInit1.executeUpdate();
            PreparedStatement psInit2 = connection.prepareStatement("CREATE TABLE accounts ( " +
                    "`first_name` VARCHAR(64) NOT NULL," +
                    "`last_name` VARCHAR(64) NOT NULL," +
                    "`mail` VARCHAR(64) NOT NULL PRIMARY KEY," +
                    "`location_id` INT NOT NULL," +
                    "`pass` BLOB(64) NOT NULL," +
                    " FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)" +
                    ");" +
                    "");
            psInit2.executeUpdate();
            for(int i =0; i < locations.length; i++){
                PreparedStatement addLocation = connection.prepareStatement(
                        "INSERT INTO locations (location_name, sess) " +
                                "VALUES (?,?);");
                addLocation.setString(1, locations[i].getName());
                addLocation.setInt(2, locations[i].getSessionNumber());
                addLocation.executeUpdate();
            }
            accountsStoreDao = new AccountsStoreDao(ds);
            for(int i =0; i < accounts.length; i++){
                accountsStoreDao.addAccount(accounts[i]);
            }
            PreparedStatement initShoppingStore = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS shop_store("+
                    "`shop_item_id` INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                    "`writer_mail` VARCHAR(64) NOT NULL," +
                    "`location_id` INT NOT NULL,"+
                    "`price` DOUBLE NOT NULL," +
                    "FOREIGN KEY (`writer_mail`) REFERENCES accounts(`mail`)," +
                    "FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`));"
            );
            initShoppingStore.executeUpdate();
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }

}
