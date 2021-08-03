package model;

public class SaveleAdvertisement implements Advertisement{
    private Location location;
    private Account writerAccount;
    private double price;

    public SaveleAdvertisement(Location location, Account account, double price){
        this.location = location;
        this.writerAccount = account;
        this.price = price;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Account getWriterAccount() {
        return writerAccount;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
