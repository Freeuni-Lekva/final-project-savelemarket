import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChatStoreDaoTest {
    Location loc1_1 = new SaveleLocation("location_1",1);
    Location loc1_2 = new SaveleLocation("location_1",2);
    Location loc2 = new SaveleLocation("location_2",1);

    Account acc1 = new StudentAccount("name_1","last_1","pass_1","mail_1@freeuni.edu.ge",loc1_1);
    Account acc2 = new StudentAccount("name_2","last_2","pass_2","mail_2@agruni.edu.ge",loc1_1);
    Account acc3 = new StudentAccount("name_3","last_3","pass_3","mail_3@freeui.edu.ge",loc1_1);
    Account acc4 = new StudentAccount("name_4","last_4","pass_4","mail_4@freeui.edu.ge",loc1_1);
    Account acc5 = new StudentAccount("name_5","last_5","pass_5","mail_5@freeui.edu.ge",loc1_2);
    Account acc6 = new StudentAccount("name_6","last_6","pass_6","mail_6@freeui.edu.ge",loc1_2);
    Account acc7 = new StudentAccount("name_7","last_7","pass_7","mail_7@freeui.edu.ge",loc2);;

    MysqlConnectionPoolDataSource ds;
    AccountsStore accStore;
    LocationStore locStore;
    ChatStore chatStore;

    @BeforeEach
    public void init(){
        ds = DatabaseInitializer.createDataSource();
        DatabaseInitializer.recreateDatabase(ds);
        chatStore = new ChatStoreDao(ds);
        accStore = new AccountsStoreDao(ds);
        locStore = new LocationStoreDao(ds);
    }

    private void addAccounts(List<Account> accounts){
        for(Account acc : accounts){
            accStore.addAccount(acc);
            chatStore.addAccounts(List.of(acc),acc.getLocation().getChatID());
            // ექაუნთის შექმნის მერე ამ ეტეაპზე ხელით ვამატებთ ბაზაში ექაუნთს ჩატზე.
        }
    }
    private void addLocations(List<Location> locations){
        for(Location loc : locations){
            int id = locStore.addLocation(loc,chatStore);
            loc.setChatID(id);
        }
    }
    private void addMessages(List<Message> messages){
        for (Message m : messages){
            int message_id = chatStore.addMessage(m);
            m.setMessageID(message_id);
        }
    }
    @Test
    public void privateChatTest(){
        List<Account> accounts = Arrays.asList(acc1,acc2);
        List<Location> locations = Arrays.asList(loc1_1);
        addLocations(locations);
        addAccounts(accounts);
        int id = chatStore.createPrivateChat(acc1,acc2);
        assertTrue(id >= 1);
        Message m1 = new GeneralMessage(acc1,"message1",false,id);
        Message m2 = new GeneralMessage(acc2,"message2",false,id);
        Message m3 = new GeneralMessage(acc2,"message2_cont",false,id);
        Message m4 = new GeneralMessage(acc1,"message3",false,id);
        List<Message> list = Arrays.asList(m1,m2,m3,m4);
        addMessages(list);
        List<Message> messageList = chatStore.getAllChatMessages(id);
        assertEquals(list, messageList);
        int id1 = chatStore.getPrivateChatID(acc1,acc2);
        int id2 = chatStore.getPrivateChatID(acc2,acc1);
        assertTrue(id1 == id2 && id1 == id);
        List<Account> members = chatStore.getChatMembers(id);
        assertTrue(members.contains(acc1));
        assertTrue(members.contains(acc2));
        assertEquals(2,chatStore.getMemberCount(id));
        List<Account> storedAccounts = chatStore.getChatMembers(id);
        assertEquals(accounts,storedAccounts);
        Chat ch = chatStore.getPrivateChat(id);
        assertEquals(ch,chatStore.getUserChats(acc1.getMail()).get(0));
        assertEquals(ch,chatStore.getUserChats(acc2.getMail()).get(0));
    }

    @Test
    public void publicChatTest(){
        // 7 is alone, 5,6 are together, 1,2,3,4 are together
        List<Account> accounts = Arrays.asList(acc1,acc2,acc3,acc4,acc5,acc6,acc7);
        List<Location> locations = Arrays.asList(loc1_1,loc1_2,loc2);
        addLocations(locations); // these accounts have only chat_id, no Chat chat.
        int id1 = loc1_1.getChatID();
        int id2 = loc1_2.getChatID();
        int id3 = loc2.getChatID();
        addAccounts(accounts);
        List<Account> firstAccs = chatStore.getChatMembers(id1);
        assertEquals(Arrays.asList(acc1,acc2,acc3,acc4),firstAccs);
        List<Account> secondAccs = chatStore.getChatMembers(id2);
        assertEquals(Arrays.asList(acc5,acc6),secondAccs);
        List<Account> thirdAcc = chatStore.getChatMembers(id3);
        assertEquals(Arrays.asList(acc7),thirdAcc);
        assertEquals(4,chatStore.getMemberCount(id1));
        assertEquals(2,chatStore.getMemberCount(id2));
        assertEquals(1,chatStore.getMemberCount(id3));
        Message m1 = new GeneralMessage(acc1,"Hey!",false,id1);
        Message m2 = new GeneralMessage(acc2,"Hello there!",false,id1);
        int mid1 = chatStore.addMessage(m1);
        int mid2 = chatStore.addMessage(m2);
        m1.setMessageID(mid1);
        m2.setMessageID(mid2);
        assertEquals(List.of(m2,m1),chatStore.getAllChatMessages(id1));
        assertEquals(List.of(m2),chatStore.getMessages(id1,1));
        assertEquals(List.of(m1),chatStore.getMessages(id1,1));
        assertEquals(0,chatStore.getMessages(id1, 1).size());
        chatStore.updateMessages(id1);
        assertEquals(List.of(m2,m1),chatStore.getMessages(id1,2));

    }
}
