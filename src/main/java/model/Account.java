package model;

public interface Account {
    /** getters **/
    String getName();
    String getPasswordHash();
    Location getLocation();
    /** setters **/
    void setLocation(Location loc);
    // add private chat
}
