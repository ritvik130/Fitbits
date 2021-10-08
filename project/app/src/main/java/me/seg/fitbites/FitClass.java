package me.seg.fitbites;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class FitClass {

    private String uid;
    //The uid of the FitClass type it belongs to
    private String fitClassTypeUid;

    public FitClass(String uid, String typeuid) {
        this.uid = uid;
        this.fitClassTypeUid = typeuid;
    }

    public FitClass() {}

    public String getUid() { return uid; }
    public String getFitClassTypeUid(){ return fitClassTypeUid; };

    public static FitClass createClass(FitClassType type){
        // creates a new Class
        //generates a uuid. likelihood of uuid collisions is essentially 0 therefore no duplicate checks
        //required.
        UUID uuid = UUID.randomUUID();

        FitClass fc = new FitClass(uuid.toString(), type.getUid());

        FirestoreDatabase.getInstance().setFitClass(fc);

        return fc;
    }

    public void deleteClass(FitClass cla){
        // deletes a class
        // access to instructor
    }

    public void editClass(String className, String description){
        // edits a class
        // access to instructor
    }
    public static void searchClass(String className, OnTaskComplete<FitClass[]> onTaskComplete){
        // returns the searched class

        //get all classes in database
        FirestoreDatabase.getInstance().getAvailableClasses(new OnTaskComplete<FitClass[]>() {
            @Override
            public void onComplete(FitClass[] classResults) {
                if(classResults != null) {
                    //get all class types
                    FirestoreDatabase.getInstance().viewAllClassTypes(new OnTaskComplete<FitClassType[]>() {
                        @Override
                        public void onComplete(FitClassType[] typeResults) {
                            if(typeResults != null) {
                                ArrayList<FitClass> resultList = new ArrayList<>();

                                classList:
                                for(FitClass c : classResults) {
                                    String tuid = c.getFitClassTypeUid();

                                    //find
                                    for(FitClassType t : typeResults) {
                                        if(t.getUid().equals(tuid)) {
                                            if(t.getClassName().contains(className)) {
                                                //add to list
                                                resultList.add(c);
                                                continue classList;
                                            }
                                        }
                                    }
                                    //uid does not exist ignore element
                                }

                                //list is full of potential search elements
                                onTaskComplete.onComplete(resultList.toArray(new FitClass[resultList.size()]));

                            } else {
                                Log.w("Process", "Something went wrong. you shouldnt be seeing this message. Code 601");
                                onTaskComplete.onComplete(null);
                            }
                        }
                    });

                } else {
                    onTaskComplete.onComplete(null);
                }
            }
        });

    }

    public void cancelClass(FitClass cla){
        //cancels a class
        // access to instructor
        System.out.println(cla+"class has been cancelled. ");
    }

    //TODO move to student
    public void enrolClass(FitClass cla){
        // enrols in class
        System.out.println(cla+"has been enrolled");
    }
    public void dropClass(FitClass cla){
        // drops class
        System.out.println(cla+"has been dropped");
    }

}