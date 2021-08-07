package DAO;

import model.Account;
import model.Chat;
import model.Message;

import java.util.List;

public interface ChatStore {
    int getPrivateChatID(String senderMail, String receiverMail); // doesn't matter which is which
    int addMessage(Message message);
    void addAccounts(List<Account> accounts, int id);
    // returns chat id
    List<Account> getChatMembers(int id);
    int createPublicChat();
    int createPrivateChat(String senderMail, String receiverMail); // order doesn't matter here either
    List<Message> getAllChatMessages(int id); // returns list of messages indexed 0 to size, 0 being the oldest.
    List<Message> getMessages(int id, int number); // keep resultset and iterate over that one. if null then do new query.
    void updateMessages(int id); // makes getMessages return new information
    int getMemberCount(int id);
    Chat getPrivateChat(int id);
    Chat getPublicChat(int id);
    List<Chat> getUserChats(String mail);
}
