import DAO.*;
import model.Account;
import model.SaveleLocation;
import model.Location;
import model.StudentAccount;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

public class AccountTest {

    @Test
    public void test() throws SQLException {
        DataSource ds = DatabaseInitializer.createDataSource("testDatabase");
        DatabaseInitializer.recreateDatabase(ds);
        AccountsStore accDao = new AccountsStoreDao(ds);
        Location l = new SaveleLocation("lokacia",2);
        ChatStore chatStore = new ChatStoreDao(ds);
        LocationStore locDao = new LocationStoreDao(ds);
        Account acc1 = new StudentAccount("a","a","pass","m1",l);
        Account acc2 = new StudentAccount("b","b","pass2","m2",l);
        locDao.addLocation(l,chatStore);
        accDao.addAccount(acc1);
        accDao.addAccount(acc2);
        int id = l.getChatID();
        chatStore.addAccounts(Arrays.asList(acc1,acc2),id);
//        System.out.println(chatStore.getChatMembers(id));

    }
}
