package me.seg.fitbites;

import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public abstract class UserData {

    public static final String USERTYPE_DATABASE_LABEL = "label";

    protected String uid;
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected int age;
    protected String address;
    protected String label = "null";

    public UserData(String uid, String firstName, String lastName, String userName, String address, int age){
        this.uid = uid;
        this.firstName=firstName;
        this.address=address;
        this.lastName=lastName;
        this.userName=userName;
        this.age=age;
    }

    public UserData() {}

    public String getUid() {return uid;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLabel() { return label; }

    public void removeAccount(){
        // removes the account of a user either instructor or member
        // access to admin

    }

}
