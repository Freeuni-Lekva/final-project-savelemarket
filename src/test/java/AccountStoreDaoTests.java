import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import model.Location;
import model.SaveleLocation;
import model.StudentAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.AccountsStoreDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountStoreDaoTests {
    /** These fields must be changed from another pc */
    private final String serverName = "localhost";
    private final int port = 3306;
    private final String dbName = "myDataBase";
    private final String user = "root";
    private final String password = "rootroot";
    ///

    private AccountsStoreDao accountsStoreDao;

    private SaveleLocation locations[] = {  new SaveleLocation("Kazbegi", 1, 1),
                                            new SaveleLocation("Kazbegi", 2, 2),
                                            new SaveleLocation("Kazbegi", 3, 3),
                                            new SaveleLocation("Tusheti", 1, 4),
                                            new SaveleLocation("Tusheti", 2, 5),
                                            new SaveleLocation("Svaneti", 1, 6),
                                            new SaveleLocation("Marelisi", 1, 7)
                                            };

    @BeforeEach
    public void init(){
        initAccountsStore();
        initLocationsStore();
    }

    @Test
    public void test1(){
        Account account1 = new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem",locations[4]);
        Account account2 = new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]);
        Account account3 = new StudentAccount("Tornike","Totladze","sanebeli123", "ttotl",locations[6]);
        accountsStoreDao.addAccount(account1);
        accountsStoreDao.addAccount(account2);
        accountsStoreDao.addAccount(account3);
        List<Account> allAccounts1 = accountsStoreDao.getAllAccounts();
        System.out.println("accounts list is: ");
        for(Account acc : allAccounts1){
            System.out.println(acc);
        }
        accountsStoreDao.removeAccount(account2);
        System.out.println("deleting account "+ account2);
        System.out.println("now accounts list is: ");
        List<Account> allAccounts2 = accountsStoreDao.getAllAccounts();
        for(Account acc : allAccounts2){
            System.out.println(acc);
        }
//        assertTrue(accountsStoreDao.hasAccount(account1.getMail()));
//        assertFalse(accountsStoreDao.hasAccount(account2.getMail()));
//        assertTrue(accountsStoreDao.hasAccount(account3.getMail()));
    }





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
                        "INSERT INTO locations (location_name, sess,location_id) " +
                                "VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
                addLocation.setString(1, locations[i].getName());
                addLocation.setInt(2, locations[i].getSessionNumber());
                addLocation.setInt(3, locations[i].getId());
                addLocation.executeUpdate();
            }
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }


}
