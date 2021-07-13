package DAO;

import model.Account;
import model.Message;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ChatStoreDao implements ChatStore{

    private DataSource dataSource;

    //queries and updates
    private static final String getPrivateChatID= "";
    private static final String getPublicChatID= "";
    private static final String addMessage = "";
    private static final String addAccounts = "";
    private static final String createPublicChat = "";
    private static final String createPrivateChat = "";

    public ChatStoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private PreparedStatement prepare(String str){
        try {
            return dataSource.getConnection().prepareStatement(str);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }
    @Override
    public int getPrivateChatID(Account sender, Account receiver) {
        PreparedStatement st = prepare(getPrivateChatID);
        if(st == null) return -1;// shouldn't happen but oh well...
        //do stuff here
        return 0;
    }
    @Override
    public int getPublicChatID(List<Account> accounts) {
        PreparedStatement st = prepare(getPublicChatID);
        if(st == null) return -1;
        //do stuff here
        return 0;
    }
    @Override
    public void addMessage(Message message, int id) {
        PreparedStatement st = prepare(addMessage);
    }

    @Override
    public void addAccounts(List<Account> accounts, int id) {
        PreparedStatement st = prepare(addAccounts);
    }

    @Override
    public int createPublicChat(List<Account> accounts) {
        PreparedStatement st = prepare(createPublicChat);
        if(st == null) return -1;
        //do stuff here
        return 0;
    }

    @Override
    public int createPrivateChat(Account sender, Account receiver) {
        PreparedStatement st = prepare(createPrivateChat);
        if(st == null) return -1;
        //do stuff here
        return 0;
    }
}
