package DAO;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import model.*;

import javax.persistence.criteria.Fetch;
import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatStoreDao extends DAO implements ChatStore {

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
    private static final String getChatAccounts = "SELECT * FROM chat_users c INNER JOIN accounts a ON c.account_mail = a.mail INNER JOIN locations l ON (a.location_id = l.location_id) INNER JOIN chat ch ON (c.chat_id = ch.chat_id) WHERE c.chat_id = ?;";
    private static final String getAllChats = "SELECT * FROM message WHERE chat_id = ? ORDER BY message_id DESC;";
    private static final String getMemberCount = "SELECT COUNT(chat_id) AS count FROM chat_users WHERE chat_id = ?";
    private static final String getUserChats = "SELECT * FROM chat_users c INNER JOIN chat ch ON c.chat_id = ch.chat_id INNER JOIN accounts a ON c.account_mail = a.mail INNER JOIN locations l ON (a.location_id = l.location_id) WHERE a.mail = ?;";
    public static final int ID_DOESNT_EXIST = 0;
    public static final int MORE_THAN_ONE_PRIVATE = -2;
    public static final int ERROR_CODE = -3;

    private Map<Integer,Integer>  fetchNumberForChat; //chat_id -> how many to fetch

    public ChatStoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public int getPrivateChatID(String senderMail, String receiverMail) {
        int id = ID_DOESNT_EXIST;
        Connection c = null;
        try {
            c = dataSource.getConnection();
            PreparedStatement st = c.prepareStatement(getPrivateChatID);
            st.setString(1,senderMail);
            st.setString(2,receiverMail);
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next()){
                id = resultSet.getInt(1);
            }
            //returned more than 2 private chats, shouldn't happen
            if(resultSet.next()){
                id = MORE_THAN_ONE_PRIVATE;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally{
            closeConnection(c);
        }
        return id;
    }

    @Override
    public int addMessage(Message message) {

        Connection c = null;
        try {
            // message(chat_id,is_picture,sent_time,message,sender_mail)
            c = dataSource.getConnection();
            PreparedStatement st = c.prepareStatement(addMessage,Statement.RETURN_GENERATED_KEYS);
            st.setInt(1,message.getChatID());
            st.setBoolean(2,message.isPicture());
            st.setString(3,message.getSendTime());
            st.setString(4,message.getText()); // if isPicture, text is link to uploaded file which we should generate
            st.setString(5,message.getSender().getMail());
            st.executeUpdate();
            return getID(st);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally{
            closeConnection(c);
        }
        System.out.println("--------------------- ADD MESSAGE FAIL ---------------------");
        return WRONG_ID;
    }

    @Override
    public void addAccounts(List<Account> accounts, int id) {
        Connection c = null;
        try {
            String update = addAccounts;
            for(int i = 0;i<accounts.size();i++){
                update += "(?,?)";
                if(i != accounts.size()-1) {
                    update+=",";
                }
            }
            update += ";";
            c = dataSource.getConnection();
            PreparedStatement st = c.prepareStatement(update);
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
        } finally{
            closeConnection(c);
        }
    }


    @Override
    public List<Account> getChatMembers(int id){
        List<Account> list= new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(getChatAccounts);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            Location l = null;
            LocationStore locationStore = new LocationStoreDao(dataSource);
            while(rs.next()){
                if(rs.getBoolean("is_private") == false){
                    if(l == null){
                        int chat_id = rs.getInt("chat_id");
                        if(chat_id == 0){
                            System.out.println("------------------chat_id is 0 -----------------------");
                        }
                        l = new SaveleLocation(rs.getString("location_name"),rs.getInt("sess"),chat_id);
                    }
                } else{
                    l = locationStore.getLocationById(rs.getInt("location_id"));
                }
                Account acc = new StudentAccount(rs.getString("first_name"),rs.getString("last_name"),
                        rs.getBytes("pass"),rs.getString("mail"),l);
                list.add(acc);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally{
            closeConnection(connection);
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
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(createPublicChat,Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();
            id = getID(st);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally{
            closeConnection(connection);
        }
        return id;
    }
    @Override
    public int createPrivateChat(String senderMail, String receiverMail) {
        System.out.println("sheqmna chati");
        int id = WRONG_ID;
        Connection c = null;
        try {
            c = dataSource.getConnection();
            //always creates new chat even if it existed before (needs change) needs
            // to do getPrivateChatID before this.
            int existingID = getPrivateChatID(senderMail,receiverMail);
            if(existingID == ID_DOESNT_EXIST) {
                PreparedStatement st = c.prepareStatement(createPrivateChat, Statement.RETURN_GENERATED_KEYS);
                st.executeUpdate();
                id = getID(st);
                System.out.println(id);
                PreparedStatement st1 = c.prepareStatement(insertIntoChatUsers);
                st1.setInt(1, id);
                st1.setString(2, senderMail);
                st1.executeUpdate();
                PreparedStatement st2 = c.prepareStatement(insertIntoChatUsers);
                st2.setInt(1, id);
                st2.setString(2, receiverMail);
                st2.executeUpdate();
            }else{
                return existingID;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally{
            closeConnection(c);
        }
        return id;
    }

    @Override
    public List<Message> getAllChatMessages(int id) {
        List<Message> list= new ArrayList<>(); // might need sorting, currently works as should without.
        AccountsStore accStore = new AccountsStoreDao(dataSource);
        Map<String, Account> accs= new HashMap<>(); // mail -> account mapping. keeps all accounts that are in chat.
        Connection c = null;
        try {
            c = dataSource.getConnection();
            PreparedStatement st = c.prepareStatement(getAllChats);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Message message = getMessage(rs,accs,accStore);
                list.add(message);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally{
            closeConnection(c);
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
    public static final int DEFAULT_FETCH_SIZE = 10;
    @Override
    public void addToMessages(int id, int number){
        if(fetchNumberForChat == null){
            fetchNumberForChat = new HashMap<>();
        }
        fetchNumberForChat.put(id,fetchNumberForChat.getOrDefault(id,DEFAULT_FETCH_SIZE) + number);
    }
    //after first call, just fetches older ones, not new ones.
    @Override
    public List<Message> getMessages(int id, int number) {
        Map<String, Account> accs= new HashMap<>(); // same mail -> account mapping as getAllChats. keeps all accounts that are in chat.
        AccountsStore accStore = new AccountsStoreDao(dataSource);
        Connection c = null;
        if(fetchNumberForChat == null){
            fetchNumberForChat = new HashMap<>();
        }
        ResultSet rs = null;
        int current = fetchNumberForChat.getOrDefault(id,DEFAULT_FETCH_SIZE);
        int newSize = current + number;
        try {
            c = dataSource.getConnection();
            PreparedStatement st = c.prepareStatement(getAllChats); // should be replaced with get (number) of chats
            st.setInt(1,id);
            rs = st.executeQuery();
            fetchNumberForChat.put(id,newSize);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection(c);
        }
        List<Message> messages = new ArrayList<>(newSize);
        try {
            for(int i = 0;i<newSize;i++) {
                if(!rs.next()){
                    fetchNumberForChat.put(id,Math.max(i,DEFAULT_FETCH_SIZE)); // this should fix integer overflow
                    closeConnection(c);
                    break;
                }
                Message message = getMessage(rs, accs, accStore);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection(c);
        }
        return messages;
    }

    @Override
    public void updateMessages(int id) {
        fetchNumberForChat.remove(id);
    }

    @Override
    public int getMemberCount(int id) {
        int output = ERROR_CODE;
        Connection c = null;
        try{
            c = dataSource.getConnection();
            PreparedStatement st = c.prepareStatement(getMemberCount);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                output = rs.getInt("count");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally{
            closeConnection(c);
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
    // if private 0th element is the mail given.
    public List<Chat> getUserChats(String mail) {
        List<Chat> chats = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(getUserChats);
            st.setString(1,mail);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Chat ch;
                int chat_id = rs.getInt("chat_id");
                if(rs.getBoolean("is_private") == true){
                    List<Account> accs = getChatMembers(chat_id);
                    if(mail.equals(accs.get(0).getMail())) {
                        ch = new PrivateChat(accs.get(0), accs.get(1),this);
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
        } finally{
          closeConnection(connection);
        }
        return chats;
    }
}
