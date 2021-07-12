package model;

import DAO.AccountsStore;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class StudentAccount implements Account {
    private String lastName;
    private String mail;


    private String name;
    private byte[] passwordBytes;
    private Location location;
    private MessageDigest md;
    // might add ნაკადი with location or have location prepared beforehand. e.g. Yazbegi-2 as Location or
    // just Yazbegi and 2 as argument as well.

    //assumes this account name is unique (need to check for store.hasAccount(name) before creating account
    //assumes password is valid
    public StudentAccount(String name, String lastName, String password, String mail, Location location) {
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

        // changes made after adding another fields
        this.lastName = lastName;
        this.mail = mail;

    }

    public StudentAccount(String name, String lastName, byte[] passwordBytes, String mail, Location location) {
        this.name = name;
        this.location = location;
        this.passwordBytes = passwordBytes;

        // changes made after adding another fields
        this.lastName = lastName;
        this.mail = mail;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public byte[] getPasswordHash() {
        return passwordBytes;
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
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentAccount that = (StudentAccount) o;
        return name.equals(that.name) && Arrays.equals(passwordBytes, that.passwordBytes) && location.equals(that.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, location);
        result = 31 * result + Arrays.hashCode(passwordBytes);
        return result;
    }

    @Override
    public String toString(){
        return "name: " + name + "  last_name: "+ lastName + "  mail: "+ mail
                + "  location: "+location.getName() + "  session: "+ location.getSessionNumber();
    }
}
