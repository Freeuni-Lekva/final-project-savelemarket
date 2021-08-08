package DAO;

import model.Notification;

import java.util.List;

public interface NotificationStore {
    List<Notification> getNotificationsFor(String mail); // notifications where receiver is the mail
    void clearAllNotificationsFor(String mail);
    void deleteNotification(int id);
    int addNotification(Notification notification);
    void acceptNotification(int id);
    void rejectNotification(int id);
}
