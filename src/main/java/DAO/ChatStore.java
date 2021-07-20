package DAO;

import model.Account;
import model.Message;

import java.util.List;

public interface ChatStore {
    int getPrivateChatID(Account sender, Account receiver); // doesn't matter which is which
    int addMessage(Message message);
    void addAccounts(List<Account> accounts, int id);
    // returns chat id
    List<Account> getChatMembers(int id);
    int createPublicChat();
    int createPrivateChat(Account sender, Account receiver); // order doesn't matter here either
}
