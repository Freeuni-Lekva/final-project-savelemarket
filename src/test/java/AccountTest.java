import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import model.SaveleLocation;
import model.Location;
import model.StudentAccount;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void test() throws SQLException {
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("testDatabase");
        ds.setUser("root");
        ds.setPassword("");
        Statement st = ds.getConnection().createStatement();
        st.executeUpdate("delete from message");
        st.executeUpdate("delete from chat_users");
        st.executeUpdate("delete from accounts");
        st.executeUpdate("delete from locations");
        st.executeUpdate("delete from chat");
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
