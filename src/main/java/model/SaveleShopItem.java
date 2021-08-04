package model;

public class SaveleShopItem implements ShopItem {
    public static Location ANY_WHERE = null;

    private Location desiredLocation;
    private Account writerAccount;
    private int id = -1;
    private double price;

    public SaveleShopItem(Account account, Location desiredLocation, double price){
        this.writerAccount = account;
        this.desiredLocation = desiredLocation;
        this.price = price;
    }

    public SaveleShopItem(int id, Account account, Location desiredLocation, double price){
        this.id = id;
        this.writerAccount = account;
        this.desiredLocation = desiredLocation;
        this.price = price;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Location getDesiredLocation() {
        return desiredLocation;
    }

    @Override
    public Account getWriterAccount() {
        return writerAccount;
    }

    @Override
    public int getAdvertId() {
        return id;
    }


    @Override
    public double getPrice() {
        return price;
    }
}
