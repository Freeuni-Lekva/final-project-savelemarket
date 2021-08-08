package model;




import DAO.ChatStore;

import java.util.List;

// Could have 2 different interfaces implementing this and those implemented by classes but this should be fine.
public abstract class Chat {

    public List<Account> getMembers(ChatStore chatStore){
        return chatStore.getChatMembers(getChatID());
    }

    static final int MSG_NUM = 20;
    abstract List<Message> getAllMessages();
    abstract List<Message> getMessages(int number);
    abstract int getChatID();
    // Removed add members for interface to work in private too (private chat doesn't need add/remove methods)
    //returns id of sent message, might need it for something idk
    abstract int sendMessage(Message message);
    //void deleteMessage(int id); // No need for it yet
    abstract int getMemberCount();
    abstract boolean isPrivate();
}
