package model;

public interface Message {
    int messageID(); // each message in chat will have unique id
    Account getSender();
    // Need to check for image somehow and think of how to implement images in Message
    String getText();
    String getSendTime();

}