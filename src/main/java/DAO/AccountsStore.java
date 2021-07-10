package DAO;

import model.Account;

import java.util.ArrayList;

public interface AccountsStore {
    void addAccount(Account account);
    void removeAccount(Account account);
    ArrayList<Account> getAllAccounts(); // maybe helpful for testing;
}
