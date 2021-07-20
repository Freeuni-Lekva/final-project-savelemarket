package DAO;

import model.*;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatStoreDao implements ChatStore{

    private DataSource dataSource;

    //queries and updates
    private static final String getPrivateChatID=
            "SELECT acc_1.chat_id FROM chat_users acc_1 INNER JOIN chat USING (chat_id) JOIN chat_users acc_2 " +
                    "ON acc_1.account_mail = ? AND acc_2.account_mail = ? AND acc_1.chat_id = acc_2.chat_id AND is_private = true;";
//    private static final String getPublicChatID= "";
    private static final String addMessage = "INSERT INTO message(chat_id,is_picture,sent_time,message,sender_mail) VALUES(?,?,?,?,?)";
    private static final String addAccounts = "INSERT INTO chat_users(chat_id,account_mail) VALUES ";
    private static final String createPublicChat = "INSERT INTO chat(is_private) VALUES(false);";
    private static final String createPrivateChat = "INSERT INTO chat(is_private) VALUES(true);";
    private static final String insertIntoChatUsers ="INSERT INTO chat_users(chat_id,account_mail) VALUES(?,?) ";
    //returns chat_id,account_mail,first_name,last_name,mail,location_id,pass blob
    private static final String getChatAccounts = "SELECT * FROM chat_users c inner join accounts a on c.account_mail = a.mail inner join locations l on (a.location_id = l.location_id) WHERE c.chat_id = ?;";

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


    @Override
    public int addMessage(Message message) {
        try {
            // message(chat_id,is_picture,sent_time,message,sender_mail)
            PreparedStatement st = dataSource.getConnection().prepareStatement(addMessage,Statement.RETURN_GENERATED_KEYS);
            st.setInt(1,message.getChatID());
            st.setBoolean(2,message.isPicture());
            st.setString(3,message.getSendTime());
            st.setString(4,message.getText()); // if isPicture, text is link to uploaded file which we should generate
            st.setString(5,message.getSender().getMail());
            st.executeUpdate();
            return st.getGeneratedKeys().getInt("message_id");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return -1;
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

    @Override
    public List<Account> getChatMembers(int id){
        List<Account> list= new ArrayList<>();
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement(getChatAccounts);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            Location l = null;
            while(rs.next()){
                if(l == null){
                    l = new SaveleLocation(rs.getString("location_name"),rs.getInt("sess"));
                }
                Account acc = new StudentAccount(rs.getString("first_name"),rs.getString("last_name"),
                        rs.getBytes("pass"),rs.getString("mail"),l);
                list.add(acc);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return list;
    }
    /**
     * creates public chat with nobody in it
     * returns id of chat
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
