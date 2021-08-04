import DAO.AccountsStore;
import DAO.AccountsStoreDao;
import DAO.ChatStore;
import DAO.ChatStoreDao;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import model.Chat;
import model.Message;
import org.junit.jupiter.api.Test;

import java.util.List;

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
//        AccountsStore accStore = new AccountsStoreDao(ds);
//        Account sender = accStore.getAccount("mail");
//        Account receiver = accStore.getAccount("mail2");
//        int id = chatStore.createPrivateChat(sender,receiver);
//        assertTrue(id != -1);
//        List<Message> lst = chatStore.getAllChatMessages(12);
//        for (Message m : lst){
//            System.out.println("(" + m.getSendTime() + ")"+ m.getSender().getMail() + ": " + m.getText());
//        }
//        System.out.println(chatStore.getMemberCount(12));
//        System.out.println(chatStore.getMemberCount(3));
//        List<Chat> chats = chatStore.getUserChats("m2");
//        for(Chat ch : chats){
//            System.out.println(ch);
//        }

    }
}
