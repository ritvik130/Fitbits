package me.seg.fitbites.data;

import java.util.ArrayList;

import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public abstract class UserData {

    public static final String USERTYPE_DATABASE_LABEL = "label";

    protected String uid;
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String age;
    protected String address;
    protected String label = "null";
    protected String password;
    protected String email;

    public UserData(String uid, String firstName, String lastName, String userName, String address, String age, String password, String email){
        this.uid = uid;
        this.firstName=firstName;
        this.address=address;
        this.lastName=lastName;
        this.userName=userName;
        this.age=age;
        this.password = password;
        this.email = email;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLabel() { return label; }

    public String getPassword() { return password; }
    public String getEmail() { return email; }

    public void removeAccount(){
        // removes the account of a user either instructor or member
        // access to admin
        AuthManager.getInstance().deleteAccount(this);
    }

    public static void searchUser(String userName, OnTaskComplete<UserData[]> onTaskComplete){

        // returns the searched class
        //get all classes in database

        FirestoreDatabase.getInstance().getUserList(new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] userData) {
                if(userName != null) {
                    ArrayList<UserData> resultList = new ArrayList<>();


                    for(UserData c : userData) {
                        if(c.getEmail().contains(userName)) {
                            resultList.add(c);
                        }
                    }
                    //list is full of potential search elements

                    onTaskComplete.onComplete(resultList.toArray(new UserData[resultList.size()]));

                } else {
                    onTaskComplete.onComplete(null);
                }
            }
        });

    }

}
