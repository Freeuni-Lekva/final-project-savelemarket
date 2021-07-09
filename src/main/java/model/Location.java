package model;

public interface Location {
    void setAccountStore(); // will be changed after creating store object, and it will be given as an argument.
    // <Type> getAccountStore(); 
    void addAccount(Account account);
    void removeAccount(Account account);
    int getSessionNumber();
}
