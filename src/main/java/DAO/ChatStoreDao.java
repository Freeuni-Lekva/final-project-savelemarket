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
    private static final String searchById = "";
    private static final String addMessage = "";
    private static final String addAccounts = "";
    private static final String createPublicChat = "";
    private static final String createPrivateChat = "";

    public ChatStoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int getPrivateChatID(Account sender, Account receiver) {
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement("");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    @Override
    public void addMessage(Message message, int id) {

    }

    @Override
    public void addAccounts(List<Account> accounts, int id) {

    }

    @Override
    public int createPublicChat(List<Account> accounts) {
        return 0;
    }

    @Override
    public int createPrivateChat(Account sender, Account receiver) {
        return 0;
    }
}
