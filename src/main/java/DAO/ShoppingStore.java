package DAO;

import model.ShopItem;

import java.util.List;

public interface ShoppingStore {
    void addAdvertisement(ShopItem shopItem);
    void removeAdvertisement(int shopItemId);
    List<ShopItem> getAllAdvertisements();
}
