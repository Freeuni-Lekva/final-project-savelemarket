package DAO;

import model.Account;
import model.Message;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.List;

public class ChatStoreDao implements ChatStore{

    private DataSource dataSource;

    //queries and updates
    private static final String getPrivateChatID=
            "select acc_1.chat_id from chat_users acc_1 inner join chat using (chat_id) join chat_users acc_2 " +
                    "on acc_1.account_mail = ? and acc_2.account_mail = ? and acc_1.chat_id = acc_2.chat_id and is_private = true;";
    private static final String getPublicChatID= "";
    private static final String addMessage = "";
    private static final String addAccounts = "INSERT INTO chat_users(chat_id,account_mail) VALUES ";
    private static final String createPublicChat = "INSERT INTO chat(is_private) VALUES(false);";
    private static final String createPrivateChat = "INSERT INTO chat(is_private) VALUES(true);";
    private static final String insertIntoChatUsers ="INSERT INTO chat_users(chat_id,account_mail) VALUES(?,?) ";

    private static final int ID_DOESNT_EXIST = 0;
    private static final int WRONG_ID = -1;
    private static final int MORE_THAN_ONE_PRIVATE = -2;
    public ChatStoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
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
        return WRONG_ID;
    }
    @Override
    public int getPrivateChatID(Account sender, Account receiver) {
        int id = ID_DOESNT_EXIST;
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement(getPrivateChatID);
            st.setString(1,sender.getMail());
            st.setString(2,receiver.getMail());
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next()){
                id = resultSet.getInt(1);
            }
            //returned more than 2 private chats, shouldn't happen
            if(resultSet.next()){
                System.out.println("------------------------------------- more than 1 private chats exist -------------------------------------");
                id = MORE_THAN_ONE_PRIVATE;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return id;
    }

    //might change this one
    @Override
    public int getPublicChatID(List<Account> accounts) {
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement(getPublicChatID);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        //do stuff here
        return WRONG_ID;
    }
    @Override
    public void addMessage(Message message, int id) {
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement(addMessage);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void addAccounts(List<Account> accounts, int id) {
        try {
            String update = addAccounts;
            for(int i = 0;i<accounts.size();i++){
                update += "(?,?)";
                if(i != accounts.size()-1) {
                    update+=",";
                }
            }
            update += ";";
            PreparedStatement st = dataSource.getConnection().prepareStatement(update);
            int i = 1;
            for(Account acc : accounts){
                st.setInt(i,id);
                i++;
                st.setString(i,acc.getMail());
                i++;
            }
            st.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * if accounts == null, create public chat with nobody in it
     * returns id of chat and puts members in it
     */
    @Override
    public int createPublicChat() {
        int id = WRONG_ID;
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement(createPublicChat,Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();
            id = getID(st);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return id;
    }
    @Override
    public int createPrivateChat(Account sender, Account receiver) {
        int id = WRONG_ID;
        try {
            Connection c = dataSource.getConnection();
            //always creates new chat even if it existed before (needs change) needs
            // to do getPrivateChatID before this.
            int existingID = getPrivateChatID(sender,receiver);
            if(existingID == ID_DOESNT_EXIST) {
                PreparedStatement st = c.prepareStatement(createPrivateChat, Statement.RETURN_GENERATED_KEYS);
                st.executeUpdate();
                id = getID(st);
                PreparedStatement st1 = c.prepareStatement(insertIntoChatUsers);
                PreparedStatement st2 = c.prepareStatement(insertIntoChatUsers);
                st1.setInt(1, id);
                st1.setString(2, sender.getMail());
                st2.setInt(1, id);
                st2.setString(1, receiver.getMail());
                st1.executeUpdate();
                st2.executeUpdate();
            }else{
                return existingID;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return id;
    }
}
