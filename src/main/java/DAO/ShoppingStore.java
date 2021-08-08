package DAO;

import model.ShoppingItem;

import java.util.List;

public interface ShoppingStore {
    void addItem(ShoppingItem shoppingItem);
    void removeItem(int shopItemId);
    void removeAllItemFor(String accountMail);
    List<ShoppingItem> getAllItemsForAccount(String accountMail);
    List<ShoppingItem> getAllItems();
<<<<<<< Updated upstream
    List<ShoppingItem> getFilteredItems(String locationName, int sessNum, double price);
=======
    List<ShoppingItem> getFilteredItems(String locationName, int sessionNum,  boolean wantToBuy, double price);
>>>>>>> Stashed changes
    public int getItemId(String writerMail, String postTime);
}
