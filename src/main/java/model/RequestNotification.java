package model;

public class RequestNotification implements Notification{
    int status;
    String sender;
    String receiver;
    Location requestLocation;
    double price;
    int id;
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
