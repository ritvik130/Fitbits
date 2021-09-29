package me.seg.fitbites;

public class GymMember extends UserData {


    public GymMember(String firstName, String lastName, String userName, String password, String address, int age){
        super(firstName, lastName, userName, password, age, address);
    }

    public void viewEnrolledClasses(){
        // gives a list of classes enrolled by the member
    }

}
