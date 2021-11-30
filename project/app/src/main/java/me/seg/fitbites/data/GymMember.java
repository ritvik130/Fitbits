package me.seg.fitbites.data;

import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class GymMember extends UserData {

    public static final String GYM_MEMBER_LABEL = "GYM_MEMBER";

    private LinkedList<String> enrolledClasses;

    public GymMember(String uid, String firstName, String lastName, String userName, String address, String age, String password, String email){
        super(uid, firstName, lastName, userName, address, age, password, email);
        this.label = GYM_MEMBER_LABEL;
        enrolledClasses = new LinkedList<>();
    }

    public GymMember() {}

    public void setEnrolledClasses(List<String> ml) { enrolledClasses = new LinkedList<>(ml); }
    public LinkedList<String> getEnrolledClasses() { return enrolledClasses; }
    public void enrollClass(FitClass ud) { enrolledClasses.add(ud.getUid()); }
    public void unenrollClass(FitClass ud) { enrolledClasses.remove(ud.getUid()); }
    public void unenrollClass(String ud){enrolledClasses.remove(ud);}

    public void checkClassCollision(FitClass fc, OnTaskComplete<Boolean> otc) {
        //object to do the collision check all at once asynchronously through the
        //callback threads

        if(enrolledClasses.size() == 0) {
            otc.onComplete(false);
        }

        class RunChecks {
            int numChecks = 0;
            boolean completed = false;
            synchronized int incrementCount() {
                numChecks++;
                return numChecks;
            }
            synchronized void complete(boolean v) {
                if(!completed) {
                    completed = true;
                    Log.w("TEST", "providing " + v);
                    otc.onComplete(v);
                }
            }
            void start() {
                for(String uid: enrolledClasses) {
                    FirestoreDatabase.getInstance().getFitClass(uid, new OnTaskComplete<FitClass>() {
                        @Override
                        public void onComplete(FitClass result) {
                            if(result != null) {
                                if(!result.getUid().equals(fc.getUid())) {
                                    //check if they collide by day
                                    if(result.getDateObj() == fc.getDateObj()) {
                                        //check if time collides
                                        if(checkTime(fc.getTime(), fc.getEndTime(), result.getTime(), result.getEndTime())) {
                                            complete(true);
                                            return;
                                        }
                                    }
                                } else {
                                    complete(true);
                                    return;
                                }
                            }

                            if(incrementCount() == enrolledClasses.size()) {
                                complete(false);
                            }
                        }
                    });
                }
            }
            boolean checkTime(int t1i, int t1f, int t2i, int t2f) {
                //if t1i is inside t2
                return (t1i < t2f && t1i > t2i) ||
                //if t1f is inside t2
                       (t1f < t2f && t1f > t2i) ||
                //if t2i is inside t1
                        (t2i < t1f && t2i > t1i) ||
                //if t2f is inside t1
                        (t2f < t1f && t2f > t1i);
            }
        }

        RunChecks rc = new RunChecks();
        rc.start();
    }

}
