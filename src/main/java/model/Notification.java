package model;

public interface Notification {
    int PENDING = 0;
    int ACCEPTED = 1;
    int REJECTED = -1;

    int getStatus();
    String getSenderMail();
    String getReceiverMail();
    Location getRequestedLocation();
    double getPrice();
    int getNotificationID();
}
