package model;

public interface ShopItem {
    void setId(int id);

    Location getDesiredLocation();
    Account getWriterAccount();
    int getAdvertId();
    double getPrice();
}
