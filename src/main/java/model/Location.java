package model;

import DAO.AccountsStore;

public interface Location {
    void addAccount(Account account);
    void removeAccount(Account account);

    AccountsStore getAccountStore();
    String getName();
    int getSessionNumber();
    //int getId();
}
