package model;

public interface Notification {
    int PENDING = 0;
    int ACCEPTED = 1;
    int REJECTED = -1;
    String PENDING_MSG = "Request is pending.";
    String ACCEPTED_MSG = "Request has been accepted.";
    String REJECTED_MSG = "Request has been declined.";
    int getStatus();
    String getSenderMail();
    String getReceiverMail();
    Location getRequestedLocation();
    double getPrice();
    boolean isPending();
    String getStatusMessage();
    int getNotificationID();
}
