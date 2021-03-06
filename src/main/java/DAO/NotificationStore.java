package DAO;

import model.Notification;

import java.util.List;

public interface NotificationStore {
    List<Notification> getPendingNotificationsFor(String mail); // notifications where receiver is the mail and isn't accepte/rejectd
    List<Notification> getNonPendingNotificationsFor(String mail);
    List<Notification> getSentNotifications(String mail);
    void clearAllNotificationsFor(String mail);
    void deleteNotification(int id);
    List<String> getParticipantMails(int id);
    boolean hasNotification(Notification notification);
    int addNotification(Notification notification);
    void acceptNotification(int id);
    void rejectNotification(int id);
}
