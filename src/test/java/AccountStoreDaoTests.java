import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import model.Location;
import model.SaveleLocation;
import model.StudentAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.AccountsStoreDao;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountStoreDaoTests {
    /** These fields must be changed from another pc */
    private final String serverName = "localhost";
    private final int port = 3306;
    private final String dbName = "myDataBase";
    private final String user = "root";
    private final String password = "rootroot";
    ///

    private AccountsStoreDao accountsStoreDao;

    private SaveleLocation locations[] = {  new SaveleLocation("Kazbegi", 1),
                                            new SaveleLocation("Kazbegi", 2),
                                            new SaveleLocation("Kazbegi", 3),
                                            new SaveleLocation("Tusheti", 1),
                                            new SaveleLocation("Tusheti", 2),
                                            new SaveleLocation("Svaneti", 1),
                                            new SaveleLocation("Marelisi", 1)
                                            };

    @BeforeEach
    public void init(){
        initAccountsStore();
        initLocationsStore();
    }


    @Test
    public void containsAccountTest(){
        Account account1 = new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem",locations[4]);
        Account account2 = new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]);
        Account account3 = new StudentAccount("Tornike","Totladze","sanebeli123", "ttotl",locations[6]);
        Account account4 = new StudentAccount("Alexa","Inauri","gori1234", "Ainau",locations[3]);
        accountsStoreDao.addAccount(account1);
        accountsStoreDao.addAccount(account2);
        accountsStoreDao.addAccount(account3);
        assertTrue(accountsStoreDao.containsAccount(account1.getMail()));
        assertTrue(accountsStoreDao.containsAccount(account2.getMail()));
        assertTrue(accountsStoreDao.containsAccount(account3.getMail()));
        assertFalse(accountsStoreDao.containsAccount(account4.getMail()));
        // remove some accounts
        accountsStoreDao.removeAccount(account1);
        accountsStoreDao.removeAccount(account2);
        assertFalse(accountsStoreDao.containsAccount(account1.getMail()));
        assertFalse(accountsStoreDao.containsAccount(account2.getMail()));
        assertTrue(accountsStoreDao.containsAccount(account3.getMail()));
        // add account4
        accountsStoreDao.addAccount(account4);
        assertTrue(accountsStoreDao.containsAccount(account4.getMail()));
        assertFalse(accountsStoreDao.containsAccount(account2.getMail()));
    }

    @Test
    public void getAllAccountsTest() throws SQLException {
        Account account1 = new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem",locations[4]);
        Account account2 = new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]);
        Account account3 = new StudentAccount("Tornike","Totladze","sanebeli123", "ttotl",locations[6]);
        accountsStoreDao.addAccount(account1);
        accountsStoreDao.addAccount(account2);
        accountsStoreDao.addAccount(account3);
        List<Account> allAccounts1 = accountsStoreDao.getAllAccounts();
        assertEquals(3, allAccounts1.size());
        System.out.println("!!!!   accounts list is:     !!!!");
        for(Account acc : allAccounts1){
            System.out.println(acc);
        }
        accountsStoreDao.removeAccount(account2);
        System.out.println("deleting account "+ account2);
        System.out.println("!!!!    now accounts list is:     !!!!");
        List<Account> allAccounts2 = accountsStoreDao.getAllAccounts();
        assertEquals(2, allAccounts2.size());
        for(Account acc : allAccounts2){
            System.out.println(acc);
        }
    }

    @Test
    public void getLocationTest(){
        Account account1 = new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem",locations[4]);
        Account account2 = new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]);
        accountsStoreDao.addAccount(account1);
        accountsStoreDao.addAccount(account2);
        List<Account> allAccounts = accountsStoreDao.getAllAccounts();
        assertTrue(allAccounts.get(0).getLocation().toString().equals("location name: Tusheti  session: 2"));
        assertTrue(allAccounts.get(1).getLocation().toString().equals("location name: Kazbegi  session: 2"));
    }

    @Test
    public void changeLocationTest(){
        Account account1 = new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem",locations[4]);
        Account account2 = new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]);
        Account account3 = new StudentAccount("Tornike","Totladze","sanebeli123", "ttotl",locations[6]);
        Account account4 = new StudentAccount("Alexa","Inauri","gori1234", "Ainau",locations[3]);
        accountsStoreDao.addAccount(account1);
        accountsStoreDao.addAccount(account2);
        accountsStoreDao.addAccount(account3);
        accountsStoreDao.addAccount(account4);
        List<Account> allAccounts1 = accountsStoreDao.getAllAccounts();
        System.out.println("!!!  before updates  !!!");
        for(Account acc : allAccounts1){
            System.out.println(acc +"  "+ acc.getLocation());
        }
        // update some locations
        accountsStoreDao.updateLocation(account1, locations[1]);
        accountsStoreDao.updateLocation(account3, locations[5]);
        List<Account> allAccounts2 = accountsStoreDao.getAllAccounts();
        System.out.println("!!!  update some locations  !!!");
        for(Account acc : allAccounts2){
            System.out.println(acc +"  "+ acc.getLocation());
        }
    }




    /**
     * Drops accounts store table down from data base
     * if it exists and then create new one.
     */
    private void initAccountsStore(){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName(serverName);
        ds.setPort(port);
        ds.setDatabaseName(dbName);
        ds.setUser(user);
        ds.setPassword(password);
        try {
            Connection connection = ds.getConnection();
            PreparedStatement psReset = connection.prepareStatement("DROP TABLE IF EXISTS accounts;");
            PreparedStatement psInit = connection.prepareStatement("CREATE TABLE accounts ( " +
                    "`first_name` VARCHAR(64) NOT NULL, " +
                    "`last_name` VARCHAR(64) NOT NULL, " +
                    "`mail` VARCHAR(64) NOT NULL PRIMARY KEY, " +
                    "`location_id` INT NOT NULL, " +
                    "`pass` BLOB(64) NOT NULL" +
                    ");" +
                    "");
            psReset.executeUpdate();
            psInit.executeUpdate();
            accountsStoreDao = new AccountsStoreDao(ds);
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }

    /** Drops if exist and then
     * creates locations table.
     * Because there is not 'LocationsStoreDao' object yet
     * saves location's field in table without creating
     * store java objects.
     * */
    private void initLocationsStore(){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName(serverName);
        ds.setPort(port);
        ds.setDatabaseName(dbName);
        ds.setUser(user);
        ds.setPassword(password);
        try {
            Connection connection = ds.getConnection();
            PreparedStatement psReset = connection.prepareStatement("DROP TABLE IF EXISTS locations;");
            PreparedStatement psInit = connection.prepareStatement("CREATE TABLE locations ( " +
                    "`location_name` VARCHAR(64) NOT NULL, " +
                    "`sess` TINYINT NOT NULL, " +
                    "`location_id` INT AUTO_INCREMENT PRIMARY KEY" +
                    ");" +
                    "");
            psReset.executeUpdate();
            psInit.executeUpdate();
            for(int i =0; i < locations.length; i++){
                PreparedStatement addLocation = connection.prepareStatement(
                        "INSERT INTO locations (location_name, sess) " +
                                "VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
                addLocation.setString(1, locations[i].getName());
                addLocation.setInt(2, locations[i].getSessionNumber());
                addLocation.executeUpdate();
            }
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }


}
