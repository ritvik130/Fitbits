package me.seg.fitbites.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDatabase {

    public static FirestoreDatabase instance;

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

    public void setUserData() {

    }

    public void setFitClass() {

    }
}
