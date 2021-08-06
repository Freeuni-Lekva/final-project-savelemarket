package model;

import java.util.List;

public class SaveleShoppingItem implements ShoppingItem {
    public static Location ANY_WHERE = null;

    private List<Location> desiredLocations;
    private Account writerAccount;
    private int id = -1;
    private double price;
    private String time;

    public SaveleShoppingItem(Account account, List<Location> desiredLocations, double price){
        this.writerAccount = account;
        this.desiredLocations = desiredLocations;
        this.price = price;
    }

    public SaveleShoppingItem(int id, String time, Account account, List<Location> desiredLocations, double price){
        this.id = id;
        this.time = time;
        this.writerAccount = account;
        this.desiredLocations = desiredLocations;
        this.price = price;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public List<Location> getDesiredLocations() {
        return desiredLocations;
    }

    @Override
    public Account getWriterAccount() {
        return writerAccount;
    }

    @Override
    public int getItemId() {
        return id;
    }


    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getCreateTime() {
        return time;
    }

    @Override
    public String toString(){
        String tempRes = writerAccount.getMail() + " wants to change " +
                writerAccount.getLocation().getName() + "" +
                writerAccount.getLocation().getSessionNumber() + " to: ";
        for(Location l : desiredLocations){
            tempRes += l.getName() + ""+
                    l.getSessionNumber() + " for "+ price + "$;  ";
        }
        if(time == null){
            return  tempRes +" not posted yet";
        }else{
            return  tempRes + " posted on: "+ time;
        }
    }
}
