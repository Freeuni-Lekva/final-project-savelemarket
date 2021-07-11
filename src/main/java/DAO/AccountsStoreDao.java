package DAO;

import model.Account;
import model.Location;

import java.util.ArrayList;

public class AccountsStoreDao implements AccountsStore {
    ///// It will be better if this file move to another package where Dao class objects will be stored.


    @Override
    public void addAccount(Account account) {

    }

    @Override
    public void removeAccount(Account account) {

    }

    @Override
    public void updateLocation(Account account, Location location) {

    }

    @Override
    public boolean hasAccount(String name) {
        return true;
    }

    @Override
    public ArrayList<Account> getAllAccounts() {
        return null;
    }
}
