package model;

public interface Chat {
    void addMember(Account account);
    void removeMember(Account account);
    
    void sendMessage(String message);
    void sendPhoto(Image image); // don't know how Image works :D
}
