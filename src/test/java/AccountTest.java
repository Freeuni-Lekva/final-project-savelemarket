import DAO.AccountsStore;
import DAO.AccountsStoreDao;
import model.Account;
import model.SaveleLocation;
import model.Location;
import model.StudentAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void baseTest(){
        String name = "name";
        String lastName = "last_name";
        String password = "pass";
        String mail = "mail@"; // added
        int locationId = 3; // added
        Location location = new SaveleLocation("lokacia",2,locationId); ///added location id
        //AccountsStore store = new AccountsStoreDao(); // commented temporarily
        AccountsStore store = null;

        Account acc = new StudentAccount(name,lastName, password,mail,location); //added lastname field
        store.addAccount(acc);
        assertEquals(name,acc.getName());
        assertEquals(location,acc.getLocation());
        acc.setLocation(store,null);
        assertNull(acc.getLocation());
        assertTrue(acc.isValidPassword(password));
        for(int i = 0;i<10;i++) {
            assertFalse(acc.isValidPassword("rand" + i));
        }
    }
}
