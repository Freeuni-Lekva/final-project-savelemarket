package DAO;

import model.ShopItem;

import javax.sql.DataSource;
import java.util.List;

public class ShoppingStoreDao implements ShoppingStore {
    private final DataSource dataSource;

    public ShoppingStoreDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void addAdvertisement(ShopItem shopItem) {

    }

    @Override
    public void removeAdvertisement(int shopItemId) {

    }

    @Override
    public List<ShopItem> getAllAdvertisements() {
        return null;
    }
}
