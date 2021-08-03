package model;

import java.util.List;

// Could have 2 different interfaces implementing this and those implemented by classes but this should be fine.
public interface Chat {

    // Removed add members for interface to work in private too (private chat doesn't need add/remove methods)
    //returns id of sent message, might need it for something idk
    int sendMessage(Message message);
    //void deleteMessage(int id); // No need for it yet
    int getMemberCount();

}
