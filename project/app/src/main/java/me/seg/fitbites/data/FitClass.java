package me.seg.fitbites.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class FitClass {

    private String uid;
    //The uid of the FitClass type it belongs to
    private String fitClassTypeUid;
    private String teacherUID;


    private Days date;
    private int time;
    private int capacity;
    private Difficulty difficulty;

    public FitClass(String uid, String typeuid, String teacherUID, Days day, int time, int maxCapacity, Difficulty difficulty) {
        this.uid = uid;
        this.fitClassTypeUid = typeuid;
        this.teacherUID = teacherUID;
        this.date = day;
        this.time = time;
        this.capacity = maxCapacity;
        this.difficulty = difficulty;
    }


    public int convertTime(int hours, int minutes){
        return hours*60 + minutes;
    }


    public FitClass() {}

    public String getUid() { return uid; }
    public String getFitClassTypeUid(){ return fitClassTypeUid; };
    public String getTeacherUID(){ return teacherUID; }
    public void setUID(String uid){ this.uid = uid; }
    public void setFitClassTypeUid(String fitClassTypeUid){ this.fitClassTypeUid = fitClassTypeUid; }
    public void setTeacherUID(String teacherUID){ this.teacherUID = teacherUID; }
    public int getTime(){ return time; }
    public void setTime(int time){ this.time = time; }
    public void setCapacity(int cap){ this.capacity = cap; }
    public int getCapacity(){ return this.capacity; }
    public void setDifficulty(Difficulty difficulty){ this.difficulty = difficulty; }
    public Difficulty getDifficulty(){ return this.difficulty; }
    public Days getDate() { return date; }
    public void setDate(Days date) { this.date = date; }




    public static FitClass createClass(FitClassType type){
        // creates a new Class
        //generates a uuid. likelihood of uuid collisions is essentially 0 therefore no duplicate checks
        //required.
        UUID uuid = UUID.randomUUID();

        FitClass fc = new FitClass(uuid.toString(), type.getUid(), null,  null, 690, 420, null);

        FirestoreDatabase.getInstance().setFitClass(fc);

        return fc;
    }

    public void deleteClass(){
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
}