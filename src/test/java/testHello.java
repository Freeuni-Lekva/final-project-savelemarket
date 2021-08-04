import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Location;
import model.SaveleLocation;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class testHello {

    private static void addLocations(List<Location> locations){
        for(Location loc : locations){
            locStore.addLocation(loc,chatStore);
        }
    }
    static MysqlConnectionPoolDataSource ds;
    static AccountsStore accStore;
    static LocationStore locStore;
    static ChatStore chatStore;
    static Location loc1_1 = new SaveleLocation("location_1",1);
    static Location loc1_2 = new SaveleLocation("location_1",2);
    static Location loc2 = new SaveleLocation("location_2",1);

    public static void main(String[] args) {

        ds = DatabaseInitializer.createDataSource();
        DatabaseInitializer.recreateDatabase(ds);
        chatStore = new ChatStoreDao(ds);
        accStore = new AccountsStoreDao(ds);
        locStore = new LocationStoreDao(ds);
        addLocations(Arrays.asList(loc1_1,loc1_2,loc2));
        System.out.println("edit configurations to  Maven -> tomcat7:run");
    }
}
