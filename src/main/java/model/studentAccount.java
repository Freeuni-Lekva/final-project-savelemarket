package model;

import DAO.AccountsStore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class studentAccount implements Account {

    private String name;
    private byte[] passwordBytes;
    private Location location;
    private MessageDigest md;
    // might add ნაკადი with location or have location prepared beforehand. e.g. Yazbegi-2 as Location or
    // just Yazbegi and 2 as argument as well.

    //assumes this account name is unique (need to check for store.hasAccount(name) before creating account
    //assumes password is valid
    public studentAccount(AccountsStore store, String name, String password, Location location) {
        try {
            md = MessageDigest.getInstance("SHA-256");
            MessageDigest mdc = (MessageDigest) md.clone();
            passwordBytes = mdc.digest(password.getBytes());
        } catch (NoSuchAlgorithmException | CloneNotSupportedException e) {
            e.printStackTrace();
            return;
        }
        this.name = name;
        this.location = location;
        store.addAccount(this); //hopefully works
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValidPassword(String password) {
        md.reset();
        byte[] check = md.digest(password.getBytes());
        return(Arrays.equals(check,passwordBytes));

    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    //might change
    public void setLocation(AccountsStore store, Location location) {
        store.updateLocation(this,location);
        this.location = location;
    }
}
