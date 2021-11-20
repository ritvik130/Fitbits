package me.seg.fitbites.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import me.seg.fitbites.firebase.AuthManager;
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
    private String className;

    public FitClass(String uid, String typeuid, String teacherUID, Days day, int time, int maxCapacity, Difficulty difficulty) {
        this.uid = uid;
        this.fitClassTypeUid = typeuid;
        this.teacherUID = teacherUID;
        this.date = day;
        this.time = time;
        this.capacity = maxCapacity;
        this.difficulty = difficulty;
    }


    public static int convertTime(int hours, int minutes){
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
    public String getClassName() {
        return className;
    }


    public void checkCollision(OnTaskComplete<Boolean> a){

        FirestoreDatabase.getInstance().getAvailableClasses(new OnTaskComplete<FitClass[]>() {
            @Override
            public void onComplete(FitClass[] result) {
                for (FitClass i: result){
                    if(i.getDate() == FitClass.this.getDate() && i.getFitClassTypeUid() == FitClass.this.getFitClassTypeUid()){
                        a.onComplete(true);
                        return;
                    }

                }

                a.onComplete(false);

            }
        });

    }


    public static FitClass createClass(FitClassType type){
        // creates a new Class
        //generates a uuid. likelihood of uuid collisions is essentially 0 therefore no duplicate checks
        //required.

        UUID uuid = UUID.randomUUID();

        FitClass fc = new FitClass(uuid.toString(), type.getUid(), null,  null, 690, 420, null);

        FirestoreDatabase.getInstance().setFitClass(fc);

        return fc;
    }

    public void updateClass(){
        // edits a class
        // access to instructor
        if(AuthManager.getInstance().getCurrentUserData() instanceof Instructor) {
            FirestoreDatabase.getInstance().setFitClass(this);
        }
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
                                    // uid does not exist ignore element
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

    public void cancelClass(){
        //cancels a class
        // access to instructor
        if(AuthManager.getInstance().getCurrentUserData() instanceof Instructor) {
            FirestoreDatabase.getInstance().deleteFitClass(this);
        }
    }

    public static void searchClassByTeacher(String lastName, OnTaskComplete<FitClass []> onTaskComplete){

        Instructor.searchInstructor(lastName, new OnTaskComplete<Instructor[]>() {
            @Override
            public void onComplete(Instructor[] instructors) {

                if(instructors == null || instructors.length == 0){
                    onTaskComplete.onComplete(null);
                    return;
                }

                FirestoreDatabase.getInstance().getAvailableClasses(new OnTaskComplete<FitClass[]>() {
                    @Override
                    public void onComplete(FitClass[] result) {
                        ArrayList<FitClass> list = new ArrayList<>();

                        outside:
                        for (FitClass i: result){
                            for (Instructor j: instructors){
                                if (i.getTeacherUID().equals(j.getUid())){
                                    list.add(i);
                                    continue outside;
                                }
                            }
                        }
                        onTaskComplete.onComplete(list.toArray(new FitClass[list.size()]));


                    }
                });
            }
        });


    }

}