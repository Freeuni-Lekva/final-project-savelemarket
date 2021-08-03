package model;

import java.text.SimpleDateFormat;

public class GeneralMessage implements Message{
    private int messageID;
    private Account sender;
    private String messageText;
    private String time;
    private boolean isPicture;
    private int chatID;

    // this constructor is used when displaying messages and getting them from Database
    public GeneralMessage(int messageID, Account sender, String messageText, boolean isPicture, int chatID, String time) {
        this.messageID = messageID;
        this.sender = sender;
        this.messageText = messageText;
        this.isPicture = isPicture;
        this.chatID = chatID;
        this.time = time;
    }

    // this constructor is used when adding to Database because messageID is non-existent
    // needs to set messageID after adding to db
    public GeneralMessage(Account sender, String messageText, boolean isPicture, int chatID){
        this.sender = sender;
        this.messageText = messageText;
        this.isPicture = isPicture;
        this.chatID = chatID;
        time = getCurrentTime();
    }


    private String getCurrentTime(){
        java.util.Date dt = new java.util.Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }

    @Override
    // always should be called after adding to database
    public void setMessageID(int messageID){
        this.messageID = messageID;
    }
    @Override
    public int messageID() {
        return messageID;
    }

    @Override
    public Account getSender() {
        return sender;
    }

    @Override
    public String getText() {
        return messageText;
    }

    @Override
    public String getSendTime() {
        return time;
    }

    @Override
    public boolean isPicture() {
        return isPicture;
    }

    @Override
    public int getChatID() {
        return chatID;
    }

    @Override
    public String toString() {
        return "GeneralMessage{" +
                "messageID=" + messageID +
                ", sender_mail=" + sender.getMail() +
                ", messageText='" + messageText + '\'' +
                ", time='" + time + '\'' +
                ", isPicture=" + isPicture +
                ", chatID=" + chatID +
                '}';
    }

}
