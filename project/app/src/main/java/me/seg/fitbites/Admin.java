package me.seg.fitbites;

public class Admin extends UserData {

    public Admin(String firstName, String lastName, String userName, String password,String address,int age) {

        // username and password as specified in the instructions
        super(firstName,lastName,"admin","admin123",age,address);
    }


}
