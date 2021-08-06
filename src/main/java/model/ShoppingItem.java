package model;

import java.util.List;

public interface ShoppingItem {
    void setId(int id);

    List<Location> getDesiredLocations();
    Account getWriterAccount();
    int getItemId();
    double getPrice();
    String getCreateTime();
}
