package model;

public interface Chat {
    void addMember(Account account);
    void removeMember(Account account);
    
    void sendMessage(Message message);
}
