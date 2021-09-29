package me.seg.fitbites;

import java.util.HashMap;

public class Instructor extends UserData {

    public Instructor(String firstName, String lastName, String userName, String password, String address, int age){
        super(firstName, lastName, userName, password, age, address);
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> result = super.toHashMap();

        //none to add

        return result;
    }
}
