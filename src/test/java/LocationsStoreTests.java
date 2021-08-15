import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Account;
import model.Location;
import model.SaveleLocation;
import model.StudentAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class LocationsStoreTests {
    private AccountsStoreDao accountsStoreDao;
    private LocationStore locationStore;

    private SaveleLocation locations[] = {  new SaveleLocation("Kazbegi", 1),
            new SaveleLocation("Kazbegi", 2),
            new SaveleLocation("Kazbegi", 3),
            new SaveleLocation("Tusheti", 1),
            new SaveleLocation("Tusheti", 2),
            new SaveleLocation("Svaneti", 1),
            new SaveleLocation("Marelisi", 1),
            new SaveleLocation("ციმბირი", 2)

    };

    private Account accounts[] = {
            new StudentAccount("Levana","Iremashvili","dzegvi123", "lirem",locations[4]),
            new StudentAccount("Nika","Shugliashvili","gori123", "nshug",locations[1]),
            new StudentAccount("Tornike","Totladze","sanebeli123", "ttotl",locations[6]),
            new StudentAccount("Alexa","Inauri","gori123", "ainau",locations[3]),
            new StudentAccount("Vano","Ganjelashvili","gori123", "vganj",locations[3])
    };


    @BeforeEach
    public void init(){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("testDatabase");
        ds.setUser("root");
        ds.setPassword("");

        initDbs(ds);
        accountsStoreDao = new AccountsStoreDao(ds);
    }

    @Test
    public void testGetLocation(){
        Location location = locationStore.getLocation(accounts[0].getMail());
        assertTrue(location.getName().equals(locations[4].getName()));
        assertEquals(locations[4].getSessionNumber(), location.getSessionNumber());
    }

    @Test
    public void testMultipleGetMethods(){
        assertEquals(locations.length, locationStore.getAllLocations().size());
        assertEquals(2, locationStore.getStudents(locations[3]).size());
        assertEquals(1, locationStore.getStudents(locations[1]).size());
        String [] locNames = {"Kazbegi", "Tusheti", "Svaneti", "Marelisi", "ციმბირი"};
        assertTrue(Arrays.asList(locNames).equals(locationStore.getUniqueLocations()));
        List<Location> possibleLocations1 = locationStore.getPossibleLocations(null,-1);
        assertEquals(locations.length, possibleLocations1.size());
        List<Location> possibleLocations2 = locationStore.getPossibleLocations("Kazbegi",-1);
        assertEquals(3, possibleLocations2.size());
        List<Location> possibleLocations3 = locationStore.getPossibleLocations(null,1);
        assertEquals(4, possibleLocations3.size());
    }

    @Test
    public void testHasLocation(){
        assertTrue(locationStore.hasLocation(locations[0].getName(), locations[0].getSessionNumber()));
        assertTrue(locationStore.hasLocation(locations[1].getName(), locations[1].getSessionNumber()));
        assertTrue(locationStore.hasLocation(locations[2].getName(), locations[2].getSessionNumber()));
        assertFalse(locationStore.hasLocation(locations[0].getName(), 9));
        assertFalse(locationStore.hasLocation("Tbilisi", locations[2].getSessionNumber()));
    }

    @Test
    public void testGetLocationId(){
        assertEquals(1, locationStore.getLocationId(locations[0].getName(), locations[0].getSessionNumber()));
        assertEquals(2, locationStore.getLocationId(locations[1].getName(), locations[1].getSessionNumber()));
        assertEquals(3, locationStore.getLocationId(locations[2].getName(), locations[2].getSessionNumber()));
        assertEquals(4, locationStore.getLocationId(locations[3].getName(), locations[3].getSessionNumber()));
    }

    @Test
    public void testGetLocationById(){
        Location l1 = locationStore.getLocationById(1);
        assertTrue(l1.getName().equals(locations[0].getName()));
        assertEquals(locations[0].getSessionNumber(), l1.getSessionNumber());

        Location l2 = locationStore.getLocationById(2);
        assertTrue(l2.getName().equals(locations[1].getName()));
        assertEquals(locations[1].getSessionNumber(), l2.getSessionNumber());

        Location l3 = locationStore.getLocationById(3);
        assertTrue(l3.getName().equals(locations[2].getName()));
        assertEquals(locations[2].getSessionNumber(), l3.getSessionNumber());
    }

    @Test
    public void testGetLocationByName(){
        Location l1 = locationStore.getLocation(locations[0].getName(), locations[0].getSessionNumber());
        assertTrue(l1.getName().equals(locations[0].getName()));
        assertEquals(locations[0].getSessionNumber(), l1.getSessionNumber());

        Location l2 = locationStore.getLocation(locations[1].getName(), locations[1].getSessionNumber());
        assertTrue(l2.getName().equals(locations[1].getName()));
        assertEquals(locations[1].getSessionNumber(), l2.getSessionNumber());

        Location l3 = locationStore.getLocation(locations[2].getName(), locations[2].getSessionNumber());
        assertTrue(l3.getName().equals(locations[2].getName()));
        assertEquals(locations[2].getSessionNumber(), l3.getSessionNumber());
    }


    private void initDbs(MysqlConnectionPoolDataSource ds){
        try {
            Connection connection = ds.getConnection();
            DatabaseInitializer dbinit = new DatabaseInitializer();
            dbinit.initialize("testDatabase");
            locationStore = new LocationStoreDao(ds);
            for(int i =0; i < locations.length; i++){
                ChatStore ch = new ChatStoreDao(ds);
                locationStore.addLocation(locations[i], ch);
            }
            accountsStoreDao = new AccountsStoreDao(ds);
            for(int i =0; i < accounts.length; i++){
                accountsStoreDao.addAccount(accounts[i]);
            }
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }
}
