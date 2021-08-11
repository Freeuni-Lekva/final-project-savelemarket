package model;




import DAO.ChatStore;

import java.util.List;

// Could have 2 different interfaces implementing this and those implemented by classes but this should be fine.
public abstract class Chat {

    public List<Account> getMembers(ChatStore chatStore){
        return chatStore.getChatMembers(getChatID());
    }

    static final int MSG_NUM = 20;
    public abstract List<Message> getAllMessages();
    public abstract List<Message> getMessages(int number);
    public abstract int getChatID();
    public abstract String getChatName(String currentMail);
    // Removed add members for interface to work in private too (private chat doesn't need add/remove methods)
    //returns id of sent message, might need it for something idk
    public abstract int sendMessage(Message message);
    //void deleteMessage(int id); // No need for it yet
    public abstract int getMemberCount();
    public abstract boolean isPrivate();
    public abstract String getChatURL();
}
