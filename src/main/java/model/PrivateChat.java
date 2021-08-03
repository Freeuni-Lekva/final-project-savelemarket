package model;

import DAO.ChatStore;

import java.util.Arrays;
import java.util.List;

public class PrivateChat implements Chat{

    private ChatStore db;
    private Account sender;
    private Account receiver;
    private int id;
    private static final String privateName = "Private Chat";
    /**
     * receives initialized ChatStore Object that has database initialized with chat ID
     * basically chat exists in database before this constructor is called
     */

    public PrivateChat(ChatStore db, Account sender, Account receiver) {
        this.db = db;
        this.sender = sender;
        this.receiver = receiver;
        this.id = db.getPrivateChatID(sender,receiver);
        db.addAccounts(Arrays.asList(sender,receiver),id);

    }

    @Override
    public List<Message> getAllMessages() {
        return db.getAllChatMessages(id);
    }

    @Override
    public String getChatName() {
        return privateName;
    }

    @Override
    public List<Message> getMessages(int number) {
        return db.getMessages(id,number);
    }

    @Override
    public int sendMessage(Message message) {
        return db.addMessage(message);
    }

    @Override
    public int getMemberCount(){
        return 2;
    }
}
