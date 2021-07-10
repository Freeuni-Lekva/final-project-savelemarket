package model;

public interface Location {
    void addAccount(Account account);
    void removeAccount(Account account);

    AccountsStore getAccountStore();
    String getName();
    int getSessionNumber();
}
