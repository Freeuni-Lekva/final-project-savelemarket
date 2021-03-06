package model;

import DAO.ChatStore;

import java.util.List;
import java.util.Objects;

public class LocationChat extends Chat{


    private ChatStore db;
    public List<Account> accounts;
    private int id;
    private String chatName;

    // for this one need to create new public chat and give id here before making new LocationChat
//    public LocationChat(ChatStore db,List<Account> accounts, int id) {
//        this.db = db;
//        this.accounts = accounts;
//        this.id = id;
//        addAccounts(accounts);
//        if(accounts.size() != 0)
//            chatName = accounts.get(0).getLocation().getName() + " " + accounts.get(0).getLocation().getSessionNumber();
//    }

    // alternate constructor for fetching from database
    public LocationChat(ChatStore db,String locationName, List<Account> accounts,int id){
        this.db = db;
        this.accounts = accounts;
        this.id = id;
        chatName = locationName;
    }

    @Override
    public int getChatID(){
        return id;
    }

    @Override
    public String getChatName(String mail) {
        // doesn't care about mail
        return chatName;
    }
    //needs to be called after alternate constructor
    @Override
    public void setChatName(String locName, int sessNum){
        chatName = locName + " " + sessNum;
    }
    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public String getChatURL() {
        return "";
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

    public void updateChatMembers(){
        accounts = db.getChatMembers(id);
    }
    @Override
    public List<Message> getMessages(int number) {
        return null;
    }

    private void addAccounts(List<Account> accounts){
        db.addAccounts(accounts,id);
    }
    @Override
    public String toString() {
        return "LocationChat{" +
                "accounts=" + accounts +
                ", id=" + id +
                ", chatName='" + chatName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationChat that = (LocationChat) o;
        return id == that.id && Objects.equals(chatName, that.chatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatName);
    }
}
