package DAO;

import model.Account;
import model.Location;

import java.util.List;

public interface AccountsStore {
    void addAccount(Account account);
    void removeAccount(Account account);
    void updateLocation(Account account, Location location);
    //need to find and change location
    boolean containsAccount(String mail); // to not have two accounts with same name (name is basically unique uni id, like nshug18 etc.
    Account getAccount(String name, byte[] hash);
    List<Account> getAllAccounts(); // maybe helpful for testing;
}
