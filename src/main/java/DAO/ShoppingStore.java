package DAO;

import model.ShoppingItem;

import java.util.List;

public interface ShoppingStore {
    void addItem(ShoppingItem shoppingItem);
    void removeItem(int shopItemId);
    void removeAllItemFor(String accountMail);
    List<ShoppingItem> getAllItemsForAccount(String accountMail);
    List<ShoppingItem> getAllItems();
    List<ShoppingItem> getFilteredItems(String locationName, int sessionNum,  boolean wantToBuy, double price);
    int getItemId(String writerMail, String postTime);
    ShoppingItem getItemById(int itemId);
}
