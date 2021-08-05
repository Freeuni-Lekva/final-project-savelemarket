package model;

public interface ShoppingItem {
    void setId(int id);

    Location getDesiredLocation();
    Account getWriterAccount();
    int getItemId();
    double getPrice();
}
