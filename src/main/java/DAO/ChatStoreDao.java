package DAO;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import model.*;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatStoreDao implements ChatStore{

    private DataSource dataSource;
    //queries and updates
    private static final String getPrivateChatID=
            "SELECT acc_1.chat_id FROM chat_users acc_1 INNER JOIN chat USING (chat_id) JOIN chat_users acc_2 " +
                    "ON acc_1.account_mail = ? AND acc_2.account_mail = ? AND acc_1.chat_id = acc_2.chat_id AND is_private = TRUE;";
    //private static final String getPublicChatID= "";
    private static final String addMessage = "INSERT INTO message(chat_id,is_picture,sent_time,message,sender_mail) VALUES(?,?,?,?,?)";
    private static final String addAccounts = "INSERT INTO chat_users(chat_id,account_mail) VALUES ";
    private static final String createPublicChat = "INSERT INTO chat(is_private) VALUES(false);";
    private static final String createPrivateChat = "INSERT INTO chat(is_private) VALUES(true);";
    private static final String insertIntoChatUsers ="INSERT INTO chat_users(chat_id,account_mail) VALUES(?,?);";
    //returns chat_id,account_mail,first_name,last_name,mail,location_id,pass blob
    private static final String getChatAccounts = "SELECT * FROM chat_users c INNER JOIN accounts a ON c.account_mail = a.mail INNER JOIN locations l ON (a.location_id = l.location_id) WHERE c.chat_id = ?;";
    private static final String getAllChats = "SELECT * FROM message WHERE chat_id = ? ORDER BY message_id;";
    private static final String getMemberCount = "SELECT COUNT(chat_id) AS count FROM chat_users WHERE chat_id = ?";
    private static final String getUserChats = "SELECT * FROM chat_users c INNER JOIN chat ch ON c.chat_id = ch.chat_id INNER JOIN accounts a ON c.account_mail = a.mail INNER JOIN locations l ON (a.location_id = l.location_id) WHERE a.mail = ?;";
    private static final int ID_DOESNT_EXIST = 0;
    private static final int WRONG_ID = -1;
    private static final int MORE_THAN_ONE_PRIVATE = -2;
    private static final int ERROR_CODE = -3;

    private Map<Integer,ResultSet>  resultSetMap; // null if not getting messages by number. not null if query is done and fetching by some amounts

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
        System.out.println("------------------------ WRONG ID -------------------------");
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
            return getID(st);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        System.out.println("--------------------- ADD MESSAGE FAIL ---------------------");
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
                st2.setString(2, receiver.getMail());
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

    @Override
    public List<Message> getAllChatMessages(int id) {
        List<Message> list= new ArrayList<>(); // might need sorting, currently works as should without.
        AccountsStore accStore = new AccountsStoreDao(dataSource);
        Map<String, Account> accs= new HashMap<>(); // mail -> account mapping. keeps all accounts that are in chat.
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement(getAllChats);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Message message = getMessage(rs,accs,accStore);
                list.add(message);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return list;
    }
    private Message getMessage(ResultSet rs, Map<String, Account> accs, AccountsStore accStore) throws SQLException {
        Account acc;
        String senderMail = rs.getString("sender_mail");
        if(accs.containsKey(senderMail)){ // account is already in map
            acc = accs.get(senderMail);
        }else{ // account isn't in map yet
            acc = accStore.getAccount(senderMail);
            accs.put(senderMail,acc);
        }
        Message message = new GeneralMessage(rs.getInt("message_id"),acc,rs.getString("message"),
                rs.getBoolean("is_picture"),rs.getInt("chat_id"),rs.getString("sent_time"));
        return message;
    }

    //after first call, just fetches older ones, not new ones.
    @Override
    public List<Message> getMessages(int id, int number) {
        Map<String, Account> accs= new HashMap<>(); // same mail -> account mapping as getAllChats. keeps all accounts that are in chat.
        AccountsStore accStore = new AccountsStoreDao(dataSource);
        if(resultSetMap.getOrDefault(id,null) == null){
            try {
                PreparedStatement st = dataSource.getConnection().prepareStatement(getAllChats);
                st.setInt(1,id);
                ResultSet set = st.executeQuery();
                set.next(); // initial next, so it gets to first element
                resultSetMap.put(id,set);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        List<Message> messages = new ArrayList<>(number);
        ResultSet rs = resultSetMap.get(id);
        try {
            for(int i = 0;i<number;i++) {
                Message message = getMessage(rs, accs, accStore);
                messages.add(message);
                if(!rs.next()){
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void updateMessages(int id) {
        resultSetMap.put(id,null);
    }

    @Override
    public int getMemberCount(int id) {
        int output = ERROR_CODE;
        try{
            PreparedStatement st = dataSource.getConnection().prepareStatement(getMemberCount);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                output = rs.getInt("count");
                rs.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return output;
    }

    //these are more like convenience methods than actual methods
    @Override
    public Chat getPrivateChat(int id) {
        List<Account> accounts = getChatMembers(id);
        Chat ch = new PrivateChat(accounts.get(0),accounts.get(1),this);
        return ch;
    }
    @Override
    public Chat getPublicChat(int id){
        List<Account> accounts = getChatMembers(id);
        Chat ch = new LocationChat(this,accounts,id);
        return ch;
    }

    @Override
    // returns chats without messages for now
    public List<Chat> getUserChats(String mail) {
        List<Chat> chats = new ArrayList<>();
        try {
            PreparedStatement st = dataSource.getConnection().prepareStatement(getUserChats);
            st.setString(1,mail);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Chat ch;
                int chat_id = rs.getInt("chat_id");
                if(rs.getBoolean("is_private") == true){
                    List<Account> accs = getChatMembers(chat_id);
                    if(mail.equals(accs.get(0).getMail())) {
                        ch = new PrivateChat( accs.get(0), accs.get(1),this);
                    }else{
                        ch = new PrivateChat(accs.get(1),accs.get(0),this);
                    }
                }else{
                    ch = getPublicChat(chat_id);
                }
                chats.add(ch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chats;
    }
}
