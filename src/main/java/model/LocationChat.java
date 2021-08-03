package model;

import DAO.ChatStore;

import java.util.List;

public class LocationChat implements Chat{

    private ChatStore db;
    private List<Account> accounts;
    private int id;
    private String chatName;

    // for this one need to create new public chat and give id here before making new LocationChat
    public LocationChat(ChatStore db,List<Account> accounts,String locationName, int id) {
        this.db = db;
        this.accounts = accounts;
        this.id = id;
        chatName = locationName;
    }

    // alternate constructor
    public LocationChat(ChatStore db, List<Account> accounts,int id){
        this.db = db;
        this.accounts = accounts;
        this.id = id;
    }

    //needs to be called after alternate constructor
    @Override
    public void setChatName(String locationName){
        chatName = locationName;
    }

    @Override
    public List<Message> getAllMessages() {
        return db.getAllChatMessages(id);
    }

    @Override
    // message already has all information like id to be added in db
    public int sendMessage(Message message) {
        return db.addMessage(message);
    }

    @Override
    public int getMemberCount() {
        return db.getMemberCount(id);
    }

    @Override
    public String getChatName() {
        return chatName;
    }

    @Override
    public List<Message> getMessages(int number) {
        return null;
    }
}
