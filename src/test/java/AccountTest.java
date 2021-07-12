import DAO.AccountsStore;
import DAO.AccountsStoreDao;
import DAO.LocationStore;
import DAO.LocationStoreDao;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import model.SaveleLocation;
import model.Location;
import model.StudentAccount;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void test() throws SQLException {
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("myDatabase");
        ds.setUser("shug");
        ds.setPassword("");
        Statement st = ds.getConnection().createStatement();
        st.executeUpdate("delete from accounts");
        st.executeUpdate("delete from locations");
        AccountsStore dao = new AccountsStoreDao(ds);
        Location l = new SaveleLocation("lokacia",2);
        LocationStore locDao = new LocationStoreDao(ds);
        locDao.addLocation(l);
        Account a = new StudentAccount("a","b","pass","mail", l);
        dao.addAccount(a);
        Account acc = dao.getAllAccounts().get(0);
        System.out.println(a);
        System.out.println(acc);
    }
}
