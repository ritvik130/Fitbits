package me.seg.fitbites.data;

import java.util.ArrayList;

import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class Instructor extends UserData {

    public static final String INSTRUCTOR_LABEL = "INSTRUCTOR";

    public Instructor(String uid, String firstName, String lastName, String userName, String address, String age, String password, String email){
        super(uid, firstName, lastName, userName, address, age, password, email);
        this.label = INSTRUCTOR_LABEL;
    }

    public Instructor() {}

    @SuppressWarnings("unchecked")
    public static void searchInstructor(String lastName, OnTaskComplete<Instructor[]> onTaskComplete){

        // returns the searched class
        // get all classes in database

        FirestoreDatabase.getInstance().getUserList(new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] instructorData) {
                if(lastName!= null) {
                    ArrayList<Instructor> resultList = new ArrayList<>();


                    for(UserData c : instructorData) {
                        if(c.getLastName().contains(lastName) && c instanceof Instructor) {
                            resultList.add((Instructor) c);
                        }
                    }
                    //list is full of potential search elements

                    onTaskComplete.onComplete(resultList.toArray(new Instructor[resultList.size()]));

                } else {
                    onTaskComplete.onComplete(null);
                }
            }
        });

    }

}
