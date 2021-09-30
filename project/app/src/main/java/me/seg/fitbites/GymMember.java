package me.seg.fitbites;

import java.util.HashMap;
import java.util.Map;

public class GymMember extends UserData {

    public static final String GYM_MEMBER_LABEL = "GYM_MEMBER";

    public GymMember(String uid, String firstName, String lastName, String userName, String address, int age){
        super(uid, firstName, lastName, userName, address, age);
        this.label = GYM_MEMBER_LABEL;
    }

    public void viewEnrolledClasses(){
        // gives a list of classes enrolled by the member
    }

}
