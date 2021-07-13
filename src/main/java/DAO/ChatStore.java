package DAO;

import model.Account;
import model.Message;

import java.util.List;

public interface ChatStore {

    int getPrivateChatID(Account sender, Account receiver); // doesn't matter which is which
    void addMessage(Message message, int id);
    void addAccounts(List<Account> accounts, int id);
    // returns chat id
    int createPublicChat(List<Account> accounts);
    int createPrivateChat(Account sender, Account receiver); // order doesn't matter here either
}
