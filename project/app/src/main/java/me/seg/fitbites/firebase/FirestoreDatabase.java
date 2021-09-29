package me.seg.fitbites.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import me.seg.fitbites.FitClass;
import me.seg.fitbites.UserData;

public class FirestoreDatabase {

    public static FirestoreDatabase instance;

    //Console debug log label
    public static final String LOG_LABEL = "Database";

    //collection names
    public static final String COLLECTION_USER_INFO = "Users";
    public static final String COLLECTION_CLASSES = "Classes";

    FirebaseFirestore db;

    public FirestoreDatabase() {

        if(instance != null) {
            db = instance.db;
        } else {
            db = FirebaseFirestore.getInstance();
        }
        instance = this;
    }

    public void getUserData(String userid) {

    }

    public void getAvaliableClasses() {

    }

    public void getFitClass() {

    }

    public void viewAllClasses() {

    }

    public void setUserData(UserData data) {
        db.collection(COLLECTION_USER_INFO)
                .add(data.toHashMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(LOG_LABEL, "Added user to database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_LABEL, "Error adding user to database: " + e);
                    }
                });
    }

    public void setFitClass(FitClass data) {
        db.collection(COLLECTION_CLASSES)
                .add(data.toHashmap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(LOG_LABEL, "Added Class to database!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_LABEL, "Error adding Class to database: " + e);
                    }
                });
    }
}
