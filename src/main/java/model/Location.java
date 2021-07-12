package model;

import DAO.AccountsStore;

public interface Location {
    void setAccountStore(AccountsStore accountsStore);
    void addAccount(Account account);
    void removeAccount(Account account);

    AccountsStore getAccountStore();
    String getName();
    int getSessionNumber();
}
