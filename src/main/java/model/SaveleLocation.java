package model;

import DAO.AccountsStore;
import DAO.AccountsStoreDao;

public class SaveleLocation implements Location{
    private int sessionNumber;
    private String name;
    private AccountsStore accountsStore;

    public SaveleLocation(String name, int sessionNumber){
        this.name = name;
        this.sessionNumber = sessionNumber;
        accountsStore = new AccountsStoreDao();
    }

    @Override
    public AccountsStore getAccountStore() {
        return accountsStore;
    }

    @Override
    public void addAccount(Account account) {
        accountsStore.addAccount(account);
    }

    @Override
    public void removeAccount(Account account) {
        accountsStore.removeAccount(account);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSessionNumber() {
        return sessionNumber;
    }
}