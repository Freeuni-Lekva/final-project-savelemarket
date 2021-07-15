import DAO.AccountsStore;
import DAO.AccountsStoreDao;
import DAO.ChatStore;
import DAO.ChatStoreDao;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatStoreDaoTest {

    @Test
    public void baseTest(){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("myDatabase");
        ds.setUser("shug");
        ds.setPassword("");
        ChatStore chatStore = new ChatStoreDao(ds);
        AccountsStore accStore = new AccountsStoreDao(ds);
        Account sender = accStore.getAccount("mail");
        Account receiver = accStore.getAccount("mail2");
        int id = chatStore.createPrivateChat(sender,receiver);
        assertTrue(id != -1);
    }
}
