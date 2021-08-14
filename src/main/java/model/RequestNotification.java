package model;

import java.util.Objects;

public class RequestNotification implements Notification{
    int status;
    String sender;
    String receiver;
    Location requestLocation;
    double price;
    int id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestNotification that = (RequestNotification) o;
        return status == that.status && Double.compare(that.price, price) == 0 && id == that.id && sender.equals(that.sender) && receiver.equals(that.receiver) && requestLocation.equals(that.requestLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, sender, receiver, requestLocation, price, id);
    }

    //before adding to database
    public RequestNotification(int status, String sender, String receiver, Location requestLocation, double price) {
        this.status = status;
        this.sender = sender;
        this.receiver = receiver;
        this.requestLocation = requestLocation;
        this.price = price;
    }
//    fetching from database
    public RequestNotification(int status, String sender, String receiver, Location requestLocation, double price, int id) {
        this(status,sender,receiver,requestLocation,price);
        this.id = id;
    }

    @Override
    public int getNotificationID(){
        return id;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getSenderMail() {
        return sender;
    }

    @Override
    public String getReceiverMail() {
        return receiver;
    }

    @Override
    public Location getRequestedLocation() {
        return requestLocation;
    }

    @Override
    public double getPrice() {
        return price;
    }
    @Override
    public String getStatusMessage(){
        if(status == Notification.PENDING) return PENDING_MSG;
        return (status == Notification.ACCEPTED ? ACCEPTED_MSG : REJECTED_MSG);
    }
    @Override
    public boolean isPending(){
        return (status == Notification.PENDING);
    }
    @Override
    public String toString() {
        return "RequestNotification{" +
                "status=" + status +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", requestLocation=" + requestLocation +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
