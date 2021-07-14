package DAO;

import model.Account;
import model.Message;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ChatStoreDao implements ChatStore{

    private DataSource dataSource;

    //queries and updates
    private static final String getPrivateChatID= "";
    private static final String getPublicChatID= "";
    private static final String addMessage = "";
    private static final String addAccounts = "";
    private static final String createPublicChat = "";
    private static final String createPrivateChat = "INSERT INTO chat(is_private) VALUES(true);";

    public ChatStoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private PreparedStatement prepare(String str, int returnKey){
        try {
            if(returnKey == -1) return dataSource.getConnection().prepareStatement(str);
            return dataSource.getConnection().prepareStatement(str,returnKey);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }
    private int getID(PreparedStatement st){
        try {
            ResultSet set = st.getGeneratedKeys();
            if(set.next()){
                return set.getInt(1);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return -1;
    }
    @Override
    public int getPrivateChatID(Account sender, Account receiver) {
        PreparedStatement st = prepare(getPrivateChatID,-1);
        if(st == null) return -1;// shouldn't happen but oh well...
        //do stuff here
        return 0;
    }

    //might change this one
    @Override
    public int getPublicChatID(List<Account> accounts) {
        PreparedStatement st = prepare(getPublicChatID,-1);
        if(st == null) return -1;
        //do stuff here
        return 0;
    }
    @Override
    public void addMessage(Message message, int id) {
        PreparedStatement st = prepare(addMessage,-1);
    }

    @Override
    public void addAccounts(List<Account> accounts, int id) {
        PreparedStatement st = prepare(addAccounts,-1);
    }

    @Override
    public int createPublicChat(List<Account> accounts) {
        PreparedStatement st = prepare(createPublicChat,-1);
        if(st == null) return -1;
        //do stuff here
        return 0;
    }
    private PreparedStatement stringSetter(PreparedStatement st, String str, int index){
         return null;
    }
    @Override
    public int createPrivateChat(Account sender, Account receiver) {
        PreparedStatement st = prepare(createPrivateChat, Statement.RETURN_GENERATED_KEYS);
        int id = getID(st);
        PreparedStatement st1 = prepare("INSERT INTO chat_users(chat_id,account_mail) VALUES(" + id + "?)",-1);
        PreparedStatement st2 = prepare("INSERT INTO chat_users(chat_id,account_mail) VALUES(" + id + "?)",-1);
//        st1.setString(1,sender.getMail());
//        st2.setString(2,sender.getMail());

        if(st == null) return -1;
        //do stuff here
        return id;
    }
}
