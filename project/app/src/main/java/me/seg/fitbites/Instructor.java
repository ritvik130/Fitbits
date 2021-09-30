package me.seg.fitbites;

import java.util.HashMap;
import java.util.Map;

public class Instructor extends UserData {

    public static final String INSTRUCTOR_LABEL = "INSTRUCTOR";

    public Instructor(String uid, String firstName, String lastName, String userName, String address, int age){
        super(uid, firstName, lastName, userName, address, age);
        this.label = INSTRUCTOR_LABEL;
    }

    public Instructor() {}
}
