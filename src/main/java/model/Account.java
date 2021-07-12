package model;

import DAO.AccountsStore;

import java.io.InputStream;

public interface Account {
    /** getters **/
    String getName();

    /** added fields, maybe changed in future*/
    String getLastName();
    String getMail();
    byte[] getPasswordHash();
    ///

    boolean isValidPassword(String password);
    Location getLocation();

    /** setters **/
    void setLocation(Location loc);

    String toString();
    int hashCode();
    // add private chat
}
