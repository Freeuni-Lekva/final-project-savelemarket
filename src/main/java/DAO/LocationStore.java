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
    List<Location> getPossibleLocations(String locationName, int sessinNum);
    //creates chat with location.
    int addLocation(Location location, ChatStore chatStore);
    int getLocationId(String locationName, int sessionNum);
    Location getLocationById(int locationId);
    Location getLocation(String locationName, int locationSession);
    //added
    boolean hasLocation(String locationName, int sessionNumber);
}
