package model;

import DAO.AccountsStore;

public interface Location {
    void addAccount(AccountsStore accountsStore, Account account);
    void removeAccount(AccountsStore accountsStore, Account account);
    void setChatID(int chatID);
    int getChatID();
    void setChat(Chat chat);
    Chat getChat();
    String getName();
    int getSessionNumber();
}
