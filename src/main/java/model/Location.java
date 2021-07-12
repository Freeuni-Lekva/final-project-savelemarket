package model;

import DAO.AccountsStore;

public interface Location {
    void addAccount(AccountsStore accountsStore, Account account);
    void removeAccount(AccountsStore accountsStore, Account account);

    String getName();
    int getSessionNumber();
}
