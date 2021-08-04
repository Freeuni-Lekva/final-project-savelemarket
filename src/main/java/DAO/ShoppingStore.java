package DAO;

import model.ShopItem;

import java.util.List;

public interface ShoppingStore {
    void addItem(ShopItem shopItem);
    void removeItem(int shopItemId);
    List<ShopItem> getAllItems();
}
