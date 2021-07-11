import DAO.AccountsStore;
import DAO.AccountsStoreDao;
import model.Account;
import model.Location;
import model.StudentAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void baseTest(){
        String name = "name";
        String password = "pass";
        Location location = new saveleLocation();
        AccountsStore store = new AccountsStoreDao();
        Account acc = new StudentAccount(store,name,password,location);
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
