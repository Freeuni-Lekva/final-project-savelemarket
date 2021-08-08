import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingStoreTests {
    private final String serverName = "localhost";
    private final int port = 3306;
    private final String dbName = "testDatabase";
    private final String user = "root";
    private final String password = "";
    private AccountsStoreDao accountsStoreDao;
    private ShoppingStore shoppingStore;
    private LocationStore locationStore;

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

//        initDbs(ds);

        shoppingStore = new ShoppingStoreDao(ds);
        locationStore = new LocationStoreDao(ds);
    }

//    @Test
//    public void tempRemoveTest(){
//        int removeId = shoppingStore.getItemId("ttotl18@freeuni.edu.ge", "2021-08-08 02:10:56");
//        System.out.println(removeId);
//        shoppingStore.removeItem(removeId);
//    }

    @Test
    public void complexShoppingItemTest(){
        System.out.println("::::: test1 :::::");
        ShoppingItem item1 = new SaveleShoppingItem(accounts[0],
                locationStore.getPossibleLocations(locations[1].getName(),locations[1].getSessionNumber()), 100);
        shoppingStore.addItem(item1);
        List<ShoppingItem> items1 = shoppingStore.getAllItemsForAccount(accounts[0].getMail());
        for(ShoppingItem sI : items1){
            System.out.println(sI);
            shoppingStore.removeItem(sI.getItemId());
        }
        System.out.println("-------");
        accountsStoreDao.updateLocation(accounts[0], locations[1]);
        Account upDtAcc1 = accountsStoreDao.getAccount(accounts[0].getMail());
        ShoppingItem item2 = new SaveleShoppingItem(upDtAcc1,
                locationStore.getPossibleLocations(locations[5].getName(),locations[5].getSessionNumber()), 200);
        shoppingStore.addItem(item2);
        List<ShoppingItem> items2 = shoppingStore.getAllItemsForAccount(upDtAcc1.getMail());
        for(ShoppingItem sI : items2){
            System.out.println(sI);
            shoppingStore.removeItem(sI.getItemId());
        }
        System.out.println("-------");
        accountsStoreDao.updateLocation(upDtAcc1, locations[5]);
        Account upDtAcc2 = accountsStoreDao.getAccount(accounts[0].getMail());
        ShoppingItem item3 = new SaveleShoppingItem(upDtAcc2,
                locationStore.getPossibleLocations(locations[7].getName(),locations[7].getSessionNumber()), 400);
        shoppingStore.addItem(item3);
        List<ShoppingItem> items3 = shoppingStore.getAllItemsForAccount(upDtAcc2.getMail());
        for(ShoppingItem sI : items3){
            System.out.println(sI);
            shoppingStore.removeItem(sI.getItemId());
        }
        System.out.println("-------");

    }

    @Test
    public void testDifferentCasesOfItems(){
        System.out.println("::::: test3 :::::");
        // wants a specific one location
        ShoppingItem item1 = new SaveleShoppingItem(accounts[0],
                locationStore.getPossibleLocations(locations[0].getName(), locations[0].getSessionNumber()), 50);
        // doesn't matter location name (only session number 1)
        ShoppingItem item2 = new SaveleShoppingItem(accounts[1],
                locationStore.getPossibleLocations(null, 1), 100);
        // doesn't matter session number (only location name 'Kazbegi')
        ShoppingItem item3 = new SaveleShoppingItem(accounts[2],
                locationStore.getPossibleLocations(locations[1].getName(), SaveleLocation.NO_OP_SESS), 300);
        // doesn't matter neither location name nor session number
        // only wants to sell own location for 500 gel
        ShoppingItem item4 = new SaveleShoppingItem(accounts[3],
                locationStore.getPossibleLocations(null, SaveleLocation.NO_OP_SESS), 500);
        shoppingStore.addItem(item1);
        shoppingStore.addItem(item2);
        shoppingStore.addItem(item3);
        shoppingStore.addItem(item4);
        List<ShoppingItem> allItems = shoppingStore.getAllItems();
        for(ShoppingItem sI : allItems){
            System.out.println(sI);
        }
        System.out.println("---nika's items---");
        List<ShoppingItem> allItemsForNika = shoppingStore.getAllItemsForAccount(accounts[1].getMail());
        for(ShoppingItem sI : allItemsForNika){
            System.out.println(sI);
        }
        System.out.println("-------");
    }

    @Test
    public void removeItemsTest(){
        System.out.println("::::: test2 :::::");
        ShoppingItem item1 = new SaveleShoppingItem(accounts[0],
                locationStore.getPossibleLocations(locations[1].getName(),locations[1].getSessionNumber()), 100);
        shoppingStore.addItem(item1);
        ShoppingItem item2 = new SaveleShoppingItem(accounts[0],
                locationStore.getPossibleLocations(locations[5].getName(),locations[5].getSessionNumber()), 200);
        shoppingStore.addItem(item2);
        ShoppingItem item3 = new SaveleShoppingItem(accounts[0],
                locationStore.getPossibleLocations(locations[7].getName(),locations[7].getSessionNumber()), 400);
        shoppingStore.addItem(item3);
        List<ShoppingItem> items1 = shoppingStore.getAllItemsForAccount(accounts[0].getMail());
        for(ShoppingItem sI : items1){
            System.out.println(sI);
        }
        shoppingStore.removeItem(items1.get(1).getItemId());
        List<ShoppingItem> items2 = shoppingStore.getAllItemsForAccount(accounts[0].getMail());
        System.out.println("::::after removing 2nd shopping item::::");
        for(ShoppingItem sI : items2){
            System.out.println(sI);
        }
        shoppingStore.removeAllItemFor(accounts[0].getMail());
        List<ShoppingItem> items3 = shoppingStore.getAllItemsForAccount(accounts[0].getMail());
        System.out.println("::::after removing all item for "+accounts[0].getMail()+"::::");
        if(items3.isEmpty()){
            System.out.println("nothing to show");
        }else{
            System.out.println("there are some items left");
        }
    }




    @Test
    public void test(){
//        shoppingStore.getFilteredItems("Kazbegi", 1, 100);
    //shoppingStore.getFilteredItems("Svaneti", 1,true, 200);
    shoppingStore.getFilteredItems(null, -1, false, -900);
    }




    private void initDbs(MysqlConnectionPoolDataSource ds){
        try {
            Connection connection = ds.getConnection();
            DatabaseInitializer.initialize();
            for(int i =0; i < locations.length; i++){
                ChatStore ch = new ChatStoreDao(ds);
                int num = ch.createPublicChat();
                PreparedStatement addLocation = connection.prepareStatement(
                        "INSERT INTO locations (location_name, sess, chat_id) " +
                                "VALUES (?,?,?);");
                addLocation.setString(1, locations[i].getName());
                addLocation.setInt(2, locations[i].getSessionNumber());
                addLocation.setInt(3,num);
                addLocation.executeUpdate();
            }
            accountsStoreDao = new AccountsStoreDao(ds);
            for(int i =0; i < accounts.length; i++){
                accountsStoreDao.addAccount(accounts[i]);
            }
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }

}
