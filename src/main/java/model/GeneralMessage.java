package model;

public class GeneralMessage implements Message{
    private int messageID;
    private Account sender;
    private String messageText;
    private String time;
    private boolean isPicture;
    private int chatID;

    // this constructor is used when displaying messages and getting them from Database
    public GeneralMessage(int messageID, Account sender, String messageText, String time, boolean isPicture, int chatID) {
        this.messageID = messageID;
        this.sender = sender;
        this.messageText = messageText;
        this.time = time;
        this.isPicture = isPicture;
        this.chatID = chatID;
    }

    // this constructor is used when ading to Database because messageID is non-existent
    public GeneralMessage(Account sender, String messageText, String time, boolean isPicture, int chatID){

    }

    @Override
    public int messageID() {
        return 0;
    }

    @Override
    public Account getSender() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public String getSendTime() {
        return null;
    }

    @Override
    public boolean isPicture() {
        return false;
    }

    @Override
    public int getChatID() {
        return 0;
    }
}
