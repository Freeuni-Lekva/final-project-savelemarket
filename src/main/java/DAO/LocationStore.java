package DAO;

import model.Account;
import model.Location;

import java.util.List;

public interface LocationStore {

    Location getLocation(Account account);
    void setLocation(AccountsStore accountsStore, Account account);
    List<Location> getAllLocations();
    List<Account> getStudents(Location location);
    List<String> getUniqueLocations();

}
