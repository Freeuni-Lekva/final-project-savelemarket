import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class testHello {

    static MysqlConnectionPoolDataSource ds;
    static AccountsStore accStore;
    static LocationStore locStore;
    static ChatStore chatStore;
    static ShoppingStore shoppingStore;
    static NotificationStore notStore;
    static Location loc1_1 = new SaveleLocation("ილიურთა",1);
    static Location loc1_2 = new SaveleLocation("ყაზბეგი",2);
    static Account acc1 = new StudentAccount("name_1","last_1","pass_1","mail_1@freeuni.edu.ge",loc1_1);
    static Account acc2 = new StudentAccount("name_2","last_2","pass_2","mail_2@agruni.edu.ge",loc1_2);

    static private void addAccounts(List<Account> accounts){
        for(Account acc : accounts){
            accStore.addAccount(acc);
            chatStore.addAccounts(List.of(acc),acc.getLocation().getChatID());
            // ექაუნთის შექმნის მერე ამ ეტეაპზე ხელით ვამატებთ ბაზაში ექაუნთს ჩატზე.
        }
    }

    static private void addLocations(List<Location> locations){
        for(Location loc : locations){
            int id = locStore.addLocation(loc,chatStore);
            loc.setChatID(id);
        }
    }

    public static void main(String[] args) {

        ds = DatabaseInitializer.createDataSource();
        DatabaseInitializer.recreateDatabase(ds);
        chatStore = new ChatStoreDao(ds);
        accStore = new AccountsStoreDao(ds);
        locStore = new LocationStoreDao(ds);
        shoppingStore = new ShoppingStoreDao(ds);
        notStore = new NotificationStoreDao(ds);
        addLocations(Arrays.asList(loc1_1,loc1_2));
        addAccounts(Arrays.asList(acc1,acc2));
        ShoppingItem shoppingItem = new SaveleShoppingItem(acc1,List.of(loc1_2),100);
        shoppingStore.addItem(shoppingItem);
        Notification notification = new RequestNotification(Notification.PENDING,acc1.getMail(),acc2.getMail(),loc1_2,100);
        notStore.addNotification(notification);
        List<Notification> notifs = notStore.getPendingNotificationsFor(acc2.getMail());
        System.out.println(notifs);
        System.out.println(notStore.getNonPendingNotificationsFor(acc2.getMail()));
        System.out.println(notStore.getPendingNotificationsFor(acc1.getMail()));
        System.out.println(notStore.getNonPendingNotificationsFor(acc1.getMail()));
        notStore.deleteNotification(notifs.get(0).getNotificationID());
        notifs = notStore.getPendingNotificationsFor(acc2.getMail());
        System.out.println(notifs);
        System.out.println(notStore.getNonPendingNotificationsFor(acc2.getMail()));
        System.out.println(notStore.getPendingNotificationsFor(acc1.getMail()));
        System.out.println(notStore.getNonPendingNotificationsFor(acc1.getMail()));
    }
}
