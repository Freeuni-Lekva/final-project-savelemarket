package model;

public class SaveleShoppingItem implements ShoppingItem {
    public static Location ANY_WHERE = null;

    private Location desiredLocation;
    private Account writerAccount;
    private int id = -1;
    private double price;
    private String time;

    public SaveleShoppingItem(Account account, Location desiredLocation, double price){
        this.writerAccount = account;
        this.desiredLocation = desiredLocation;
        this.price = price;
    }

    public SaveleShoppingItem(int id, String time, Account account, Location desiredLocation, double price){
        this.id = id;
        this.time = time;
        this.writerAccount = account;
        this.desiredLocation = desiredLocation;
        this.price = price;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Location getDesiredLocation() {
        return desiredLocation;
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
                writerAccount.getLocation().getSessionNumber() + " to " +
                desiredLocation.getName() + ""+
                desiredLocation.getSessionNumber() + " for "+ price + "$";
        if(time == null){
            return  tempRes +" not posted yet";
        }else{
            return  tempRes + " posted on: "+ time;
        }
    }
}
