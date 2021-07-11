package model;

import DAO.AccountsStore;

public interface Account {
    /** getters **/
    String getName();
    boolean isValidPassword(String password);
    Location getLocation();
    /** setters **/
    void setLocation(AccountsStore store, Location loc);

    String toString();
    int hashCode();
    // add private chat
}
