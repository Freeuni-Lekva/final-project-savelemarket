package DAO;

import model.Account;
import model.Location;
import model.SaveleLocation;

import java.util.List;

public interface LocationStore {

    Location getLocation(Account account);
    List<Location> getAllLocations();
    List<Account> getStudents(Location location);
    List<String> getUniqueLocations();
    void addLocation(Location location);
}
