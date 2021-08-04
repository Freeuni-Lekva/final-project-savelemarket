package DAO;

import model.ShoppingItem;

import java.util.List;

public interface ShoppingStore {
    void addItem(ShoppingItem shoppingItem);
    void removeItem(int shopItemId);
    List<ShoppingItem> getAllItems();
}
