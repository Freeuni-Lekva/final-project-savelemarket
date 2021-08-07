import DAO.ChatStore;
import DAO.ChatStoreDao;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import model.Location;
import model.SaveleLocation;
import model.StudentAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.AccountsStoreDao;

import javax.sql.DataSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountStoreDaoTests {

    private AccountsStoreDao accountsStoreDao;

    private SaveleLocation locations[] = {  new SaveleLocation("Kazbegi", 1),
                                            new SaveleLocation("Kazbegi", 2),
                                            new SaveleLocation("Kazbegi", 3),
                                            new SaveleLocation("Tusheti", 1),
                                            new SaveleLocation("Tusheti", 2),
                                            new SaveleLocation("Svaneti", 1),
                                            new SaveleLocation("Marelisi", 1),
                                            new SaveleLocation("ციმბირი", 2)

                                            };
    private int CHAT_ID;

    @BeforeEach
    public void init(){
        DatabaseInitializer.initialize();
        MysqlConnectionPoolDataSource ds = DatabaseInitializer.createDataSource();
        ChatStore chatStore = new ChatStoreDao(ds);
        CHAT_ID = chatStore.createPublicChat();
        Arrays.stream(locations).forEach(x -> x.setChatID(CHAT_ID));
        accountsStoreDao = new AccountsStoreDao(ds);
        initLocationsStore(ds);
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
        assertEquals("location name: Tusheti  session: 2  chat_id: " + CHAT_ID,allAccounts.get(0).getLocation().toString());
        assertEquals("location name: Kazbegi  session: 2  chat_id: " + CHAT_ID,allAccounts.get(1).getLocation().toString());
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

    @Test
    public void testGetAccount(){
        Account account1 = new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem18@freeuni.edu.ge",locations[4]);
        Account account2 = new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]);
        Account account3 = new StudentAccount("Tornike","Totladze","sanebeli123", "ttotl",locations[6]);
        Account account4 = new StudentAccount("იოსებ","ჯუღაშვილი","gori123", "stalini@freeuni.edu.ge",locations[7]);

        accountsStoreDao.addAccount(account1);
        accountsStoreDao.addAccount(account2);
        accountsStoreDao.addAccount(account3);
        accountsStoreDao.addAccount(account4);
        System.out.println(accountsStoreDao.getAccount("lirem18@freeuni.edu.ge"));
        accountsStoreDao.removeAccount(account1);
        assertEquals(null, accountsStoreDao.getAccount("lirem18@freeuni.edu.ge"));
    }






    /** Drops if exist and then
     * creates locations table.
     * Because there is not 'LocationsStoreDao' object yet
     * saves location's field in table without creating
     * store java objects.
     * */
    private void initLocationsStore(MysqlConnectionPoolDataSource ds){
        try {
            Connection connection = ds.getConnection();
            for(int i =0; i < locations.length; i++){
                PreparedStatement addLocation = connection.prepareStatement(
                        "INSERT INTO locations (location_name, sess, chat_id) " +
                                "VALUES (?,?,?);");
                addLocation.setString(1, locations[i].getName());
                addLocation.setInt(2, locations[i].getSessionNumber());
                addLocation.setInt(3,locations[i].getChatID());
                addLocation.executeUpdate();
            }
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }


}