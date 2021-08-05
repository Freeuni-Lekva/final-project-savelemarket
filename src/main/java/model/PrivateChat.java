package model;

import DAO.ChatStore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PrivateChat implements Chat{

    private ChatStore db;
    private Account sender;
    private Account receiver;
    private int id;
    private  String chatName;
    /**
     * receives initialized ChatStore Object that has database initialized with chat ID
     * basically chat exists in database before this constructor is called
     */
    //this for adding to database
    public PrivateChat(ChatStore db, Account sender, Account receiver) {
        this.db = db;
        this.sender = sender;
        this.receiver = receiver;
        this.id = db.getPrivateChatID(sender,receiver);
        db.addAccounts(Arrays.asList(sender,receiver),id);
        this.chatName = Chat.PRIVATE_NAME;
    }

    //this constructor for getting from database
    public PrivateChat(Account sender,Account receiver,ChatStore db){
        this.sender = sender;
        this.receiver = receiver;
        this.id = db.getPrivateChatID(sender,receiver);
        this.chatName = Chat.PRIVATE_NAME;
    }
    @Override
    public int getChatID(){
        return id;
    }
    @Override
    public List<Message> getAllMessages() {
        return db.getAllChatMessages(id);
    }

    @Override
    public String getChatName() {
        return chatName;
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

    @Override
    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    @Override
    public boolean isPrivate() {
        return true;
    }

    @Override
    public String toString() {
        return "PrivateChat{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", id=" + id +
                ", chatName='" + chatName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateChat that = (PrivateChat) o;
        return id == that.id &&
                ((sender.equals(that.sender)  && receiver.equals(that.receiver)) || (sender.equals(that.receiver) && receiver.equals(that.sender)))
                && chatName.equals(that.chatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, id, chatName);
    }
}
