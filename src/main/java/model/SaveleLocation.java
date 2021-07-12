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
        this.accountsStore = null;
    }

    @Override
    public void setAccountStore(AccountsStore accountsStore) {
        this.accountsStore = accountsStore;
    }

    /** Works when AccountStore object is already set. */
    @Override
    public AccountsStore getAccountStore() {
        return accountsStore;
    }

    /** Works when AccountStore object is already set. */
    @Override
    public void addAccount(Account account) {
        if(accountsStore != null) accountsStore.addAccount(account);
    }

    /** Works when AccountStore object is already set. */
    @Override
    public void removeAccount(Account account) {
        if(accountsStore != null) accountsStore.removeAccount(account);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSessionNumber() {
        return sessionNumber;
    }


    @Override
    public String toString(){
        return "location name: "+name+"  session: "+sessionNumber;
    }
}
