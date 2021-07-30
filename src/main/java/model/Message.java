package model;

public interface Message {
    int messageID(); // each message in chat will have unique id
    Account getSender();
    // Need to check for image somehow and think of how to implement images in Message
    String getText(); // Message Text works as picture URL to file path if isPicture == true.
    String getSendTime();
    boolean isPicture(); // if isPicture, text could save file path or something like that.
    int getChatID();
    void setMessageID(int messageID);

}