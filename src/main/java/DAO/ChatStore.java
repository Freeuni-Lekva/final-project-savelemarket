package DAO;

import model.Account;
import model.Message;

import java.util.List;

public interface ChatStore {
    int getPublicChatID(List<Account> accounts);
    int getPrivateChatID(Account sender, Account receiver); // doesn't matter which is which
    void addMessage(Message message, int id);
    void addAccounts(List<Account> accounts, int id);
    // returns chat id
    int createPublicChat();
    int createPrivateChat(Account sender, Account receiver); // order doesn't matter here either
}
