package me.seg.fitbites;

import java.util.HashMap;
import java.util.Map;

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

    public static FitClass createClass(){
        // creates a new Class
        // access to admin
        return null;
    }

    public void deleteClass(FitClass cla){
        // deletes a class
        // access to admin
    }

    public void editClass(String className, String description){
        // edits a class
        // access to admin
    }
    public FitClass searchClass(String className){
        // returns the searched class
        return null;
    }

    public void cancelClass(FitClass cla){
        //cancels a class
        // access to instructor
        System.out.println(cla+"class has been cancelled. ");
    }
    public void enrolClass(FitClass cla){
        // enrols in class
        System.out.println(cla+"has been enrolled");
    }
    public void dropClass(FitClass cla){
        // drops class
        System.out.println(cla+"has been dropped");
    }

}