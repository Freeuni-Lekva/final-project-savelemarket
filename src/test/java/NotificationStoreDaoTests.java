import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationStoreDaoTests {

    Location loc1 = new SaveleLocation("location_1",1);
    Location loc2 = new SaveleLocation("location_2",1);
    Location loc3 = new SaveleLocation("location_3",3);
    Account acc1 = new StudentAccount("name_1","last_1","pass_1","1@freeuni.edu.ge",loc1);
    Account acc2 = new StudentAccount("name_2","last_2","pass_2","2@agruni.edu.ge",loc2);
    Account acc3 = new StudentAccount("name_3","last_3","pass3","3@freeuni.edu.ge",loc3);
    AccountsStore accStore;
    LocationStore locStore;
    ChatStore chatStore;
    NotificationStore notifStore;


    // ალბათ ამ ორი მეთოდის გატანა შეიძლება DatabaseInitializer-ში (და სხვა add(/model/-ის კლასი)-ების)
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

    @BeforeEach
    public void init(){
        DatabaseInitializer dbinit = new DatabaseInitializer();
        MysqlConnectionPoolDataSource ds = dbinit.createDataSource("testDatabase");
        dbinit.recreateDatabase(ds);
        accStore = new AccountsStoreDao(ds);
        locStore = new LocationStoreDao(ds);
        chatStore = new ChatStoreDao(ds);
        notifStore = new NotificationStoreDao(ds);
    }

    @Test
    // simulates how we do location exchange
    public void testExtensiveLocationExchange(){
        addLocations(List.of(loc1,loc2));
        addAccounts(List.of(acc1,acc2));
        Notification beforeAdding = new RequestNotification(Notification.PENDING,acc1.getMail(),acc2.getMail(),acc2.getLocation(),0);
        notifStore.addNotification(beforeAdding);
        Notification n = notifStore.getSentNotifications(acc1.getMail()).get(0);
        assertNotEquals(null,n);
        List<Notification> accNotifs1 = notifStore.getSentNotifications(acc1.getMail());
        List<Notification> accNotifs2 = notifStore.getPendingNotificationsFor(acc2.getMail());
        assertEquals(1,accNotifs1.size());
        assertEquals(1,accNotifs2.size());
        assertEquals(n,accNotifs1.get(0));
        assertEquals(accNotifs1.get(0),accNotifs2.get(0));
        List<Notification> nonPending1 = notifStore.getNonPendingNotificationsFor(acc1.getMail());
        List<Notification> nonPending2 = notifStore.getNonPendingNotificationsFor(acc2.getMail());
        List<Notification> receivedPending1 = notifStore.getPendingNotificationsFor(acc1.getMail());
        assertEquals(nonPending1,nonPending2); // is empty
        assertEquals(receivedPending1,nonPending2); // is empty
        notifStore.acceptNotification(n.getNotificationID()); // needs to update locations after this
        accStore.updateLocation(acc1,loc2,loc1.getChatID());
        accStore.updateLocation(acc2,loc1,loc2.getChatID());
        // locations have been exchanged
        acc1 = accStore.getAccount(acc1.getMail());
        acc2 = accStore.getAccount(acc2.getMail());
        assertEquals(loc2,acc1.getLocation());
        assertEquals(loc1,acc2.getLocation());
        List<Notification> sentNotifs1 = notifStore.getSentNotifications(acc1.getMail()); // not empty
        List<Notification> sentNotifs2 = notifStore.getSentNotifications(acc2.getMail()); // empty
        List<Notification> pendingNotifs1 = notifStore.getPendingNotificationsFor(acc1.getMail()); // empty
        List<Notification> pendingNotifs2 = notifStore.getPendingNotificationsFor(acc2.getMail()); // empty
        List<Notification> nonPendingNotifs1 = notifStore.getNonPendingNotificationsFor(acc1.getMail()); // not empty
        List<Notification> nonPendingNotifs2 = notifStore.getNonPendingNotificationsFor(acc2.getMail()); // empty
        assertEquals(Notification.ACCEPTED,sentNotifs1.get(0).getStatus());
        assertTrue(sentNotifs2.isEmpty());
        assertTrue(notifStore.hasNotification(sentNotifs1.get(0)));
        assertEquals(pendingNotifs1,sentNotifs2);
        assertEquals(pendingNotifs1,pendingNotifs2);
        assertEquals(nonPendingNotifs2,pendingNotifs2);
        assertEquals(nonPendingNotifs1,sentNotifs1);
        notifStore.deleteNotification(sentNotifs1.get(0).getNotificationID());
        // all is empty
        assertTrue(notifStore.getSentNotifications(acc1.getMail()).isEmpty());
        assertEquals(notifStore.getSentNotifications(acc1.getMail()),notifStore.getSentNotifications(acc2.getMail()));
        assertEquals(notifStore.getSentNotifications(acc1.getMail()),notifStore.getPendingNotificationsFor(acc1.getMail()));
        assertEquals(notifStore.getPendingNotificationsFor(acc1.getMail()),notifStore.getPendingNotificationsFor(acc2.getMail()));
        assertEquals(notifStore.getPendingNotificationsFor(acc1.getMail()),notifStore.getNonPendingNotificationsFor(acc1.getMail()));
        assertEquals(notifStore.getNonPendingNotificationsFor(acc1.getMail()),notifStore.getNonPendingNotificationsFor(acc2.getMail()));

    }

    @Test
    // simulates declined notifications
    public void nonExtensiveTest(){
        addLocations(List.of(loc1,loc2,loc3));
        addAccounts(List.of(acc1,acc2,acc3));
        Notification notif12 = new RequestNotification(Notification.PENDING,acc1.getMail(),acc2.getMail(),loc2,-100);
        Notification notif23 = new RequestNotification(Notification.PENDING,acc2.getMail(),acc3.getMail(),loc3,0);
        Notification notif31 = new RequestNotification(Notification.PENDING,acc3.getMail(),acc1.getMail(),loc1,200);
        int id12 = notifStore.addNotification(notif12);
        int id23 = notifStore.addNotification(notif23);
        int id31 = notifStore.addNotification(notif31);
        notif12 = new RequestNotification(Notification.PENDING, acc1.getMail(),acc2.getMail(),loc2,-100,id12);
        notif23 = new RequestNotification(Notification.PENDING,acc2.getMail(),acc3.getMail(),loc3,0,id23);
        notif31 = new RequestNotification(Notification.PENDING,acc3.getMail(),acc1.getMail(),loc1,200,id31);
        assertTrue(notifStore.hasNotification(notif12));
        assertTrue(notifStore.hasNotification(notif23));
        assertTrue(notifStore.hasNotification(notif31));
        List<String> accounts12 = notifStore.getParticipantMails(id12);
        List<String> accounts23 = notifStore.getParticipantMails(id23);
        List<String> accounts31 = notifStore.getParticipantMails(id31);
        assertTrue(accounts12.contains(acc1.getMail()));
        assertTrue(accounts12.contains(acc2.getMail()));
        assertTrue(accounts23.contains(acc2.getMail()));
        assertTrue(accounts23.contains(acc3.getMail()));
        assertTrue(accounts31.contains(acc3.getMail()));
        assertTrue(accounts31.contains(acc1.getMail()));
        notifStore.rejectNotification(id12);
        notifStore.rejectNotification(id23);
        notifStore.rejectNotification(id31);
        List<Notification> emptyList = new ArrayList<>();
        assertEquals(emptyList,notifStore.getPendingNotificationsFor(acc1.getMail()));
        assertEquals(emptyList,notifStore.getPendingNotificationsFor(acc2.getMail()));
        assertEquals(emptyList,notifStore.getPendingNotificationsFor(acc3.getMail()));

        assertEquals(notifStore.getSentNotifications(acc1.getMail()),notifStore.getNonPendingNotificationsFor(acc1.getMail()));
        assertEquals(notifStore.getSentNotifications(acc2.getMail()),notifStore.getNonPendingNotificationsFor(acc2.getMail()));
        assertEquals(notifStore.getSentNotifications(acc3.getMail()),notifStore.getNonPendingNotificationsFor(acc3.getMail()));

        notifStore.clearAllNotificationsFor(acc1.getMail()); // where sender is acc1
        assertEquals(emptyList,notifStore.getPendingNotificationsFor(acc1.getMail()));
        notifStore.clearAllNotificationsFor(acc2.getMail());
        assertEquals(emptyList,notifStore.getSentNotifications(acc1.getMail()));
        assertEquals(emptyList,notifStore.getNonPendingNotificationsFor(acc1.getMail()));
    }
}
