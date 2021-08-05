package model;

import DAO.AccountsStore;
import DAO.AccountsStoreDao;

public class SaveleLocation implements Location{

    private int sessionNumber;
    private String name;
    private int chatID;
    private Chat chat; // location chat

    //need to set chat after creating it
    public SaveleLocation(String name, int sessionNumber,Chat chat){
        this.name = name;
        this.sessionNumber = sessionNumber;
        this.chat = chat;
    }
    public SaveleLocation(String name, int sessionNumber){
        this.name = name;
        this.sessionNumber = sessionNumber;
    }

    @Override
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public int getChatID(){
        return chatID;
    }
    @Override
    public void setChatID(int chatID){
        this.chatID = chatID;
    }
    /** Works when AccountStore object is already set. */
    @Override
    public void addAccount(AccountsStore accountsStore, Account account) {
        accountsStore.addAccount(account);
    }

    /** Works when AccountStore object is already set. */
    @Override
    public void removeAccount(AccountsStore accountsStore, Account account) {
        accountsStore.removeAccount(account);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSessionNumber() {
        return sessionNumber;
    }


    @Override
    public String toString(){
        return "location name: "+name+"  session: "+sessionNumber;
    }
}
