package model;

public interface Notification {
    int PENDING = 0; // could also make 3 different classes implementing this but...
    int ACCEPTED = 1;
    int REJECTED = -1;
    String PENDING_MSG = "მოთხოვნა გაგზავნილია.";
    String ACCEPTED_MSG = "მოთხოვნა დადასტურებულია.";
    String REJECTED_MSG = "მოთხოვნა უარყოფილია.";
    int getStatus();
    String getSenderMail();
    String getReceiverMail();
    Location getRequestedLocation();
    double getPrice();
    boolean isPending();
    String getStatusMessage();
    int getNotificationID();
}
