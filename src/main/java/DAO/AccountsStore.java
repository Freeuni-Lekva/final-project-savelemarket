package DAO;

import model.Account;
import model.Location;

import java.util.List;

public interface AccountsStore {
    void addAccount(Account account);
    void removeAccount(Account account);
    void updateLocation(Account account, Location location, int oldID);

    boolean containsAccount(String mail); // to not have two accounts with same name (name is basically unique uni id, like nshug18 etc.
    Account getAccount(String mail);
    List<Account> getAllAccounts(); // maybe helpful for testing;
    boolean isAdmin(String username, String password);
}
