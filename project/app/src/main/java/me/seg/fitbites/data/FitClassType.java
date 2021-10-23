package me.seg.fitbites.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class FitClassType {
    private String uid;
    private String className;
    private String description;

    public FitClassType(String uid, String className, String description) {
        this.uid = uid;
        this.className = className;
        this.description = description;
    }

    public FitClassType() {}

    public String getUid() {
        return uid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void updateDatabase() {
        if(AuthManager.getInstance().getCurrentUserData() instanceof Admin) {
            FirestoreDatabase.getInstance().setFitClassType(this);
        } else {
            Log.w("Fit-Process", "Only logged in admins can update the classtype");
        }
    }

    public static void searchClass(String className, OnTaskComplete<FitClassType[]> onTaskComplete){
        // returns the searched class

        //get all classes in database
        FirestoreDatabase.getInstance().viewAllClassTypes(new OnTaskComplete<FitClassType[]>() {
            @Override
            public void onComplete(FitClassType[] classResults) {
                if(classResults != null) {
                    ArrayList<FitClassType> resultList = new ArrayList<>();


                    for(FitClassType c : classResults) {
                        if(c.getClassName().contains(className)) {
                            resultList.add(c);
                        }
                    }

                    //list is full of potential search elements
                    onTaskComplete.onComplete(resultList.toArray(new FitClassType[resultList.size()]));

                } else {
                    onTaskComplete.onComplete(null);
                }
            }
        });

    }

    public static FitClassType createFitClassType(String name, String description) {
        //generate new random uid
        //note the probability of having uid collisions is extremely low (according to various sources)
        //and basically 0 to the point where checking if there is a duplicate is generally not required.
        //also if we were soo lucky to get a uid collision, it was probably fate that the other
        //class type is overridden, also you should probably buy a lottery ticket.
        UUID uid = UUID.randomUUID();

        FitClassType ct = new FitClassType(uid.toString(), name, description);

        FirestoreDatabase.getInstance().setFitClassType(ct);

        return ct;
    }


}
