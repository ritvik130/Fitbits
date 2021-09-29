package me.seg.fitbites;

import java.util.HashMap;

public class GymMember extends UserData {


    public GymMember(String firstName, String lastName, String userName, String password, String address, int age){
        super(firstName, lastName, userName, password, age, address);
    }

    public void viewEnrolledClasses(){
        // gives a list of classes enrolled by the member
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> result = super.toHashMap();

        //none to add

        return result;
    }

}
