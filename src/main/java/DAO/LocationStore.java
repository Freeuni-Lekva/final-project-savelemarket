package DAO;

import model.Account;
import model.Location;
import model.SaveleLocation;

import java.sql.Connection;
import java.util.List;

public interface LocationStore {

    Location getLocation(String mail);
    List<Location> getAllLocations();
    List<Account> getStudents(Location location);
    List<String> getUniqueLocations();
    //creates chat with location.
    void addLocation(Location location, ChatStore chatStore);
    int getLocationId(String locationName, int sessionNum);
    Location getLocationById(int locationId);
    Location getLocation(String locationName, int locationSession);
    //added
    boolean hasLocation(String locationName, int sessionNumber);
}
