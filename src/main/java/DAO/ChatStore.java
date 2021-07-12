package DAO;

import model.Account;
import model.Message;

import java.util.List;

public interface ChatStore {

    int getChatID();
    void addMessage(Message message,int id);
    void addAccounts(List<Account> accounts);

}
