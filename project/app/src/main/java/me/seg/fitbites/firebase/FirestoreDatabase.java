package me.seg.fitbites.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import me.seg.fitbites.*;

public class FirestoreDatabase {

    public static FirestoreDatabase instance;

    //Console debug log label
    public static final String LOG_LABEL = "Database";

    //collection names
    public static final String COLLECTION_USER_INFO = "Users";
    public static final String COLLECTION_CLASSES = "Classes";
    public static final String COLLECTION_CLASS_TYPES = "ClassTypes";

    FirebaseFirestore db;

    public FirestoreDatabase() {

        if(instance != null) {
            db = instance.db;
        } else {
            db = FirebaseFirestore.getInstance();
        }
        instance = this;
    }

    public void getUserData(String userid, OnTaskComplete<UserData> onTaskComplete) {
        if(userid.equals("Admin")) {
            onTaskComplete.onComplete(new Admin());
            return;
        }

        db.collection(COLLECTION_USER_INFO)
                .document(userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot.exists()) {
                                String type = documentSnapshot.get(UserData.USERTYPE_DATABASE_LABEL, String.class);

                                UserData result;

                                switch (type) {
                                    case Instructor.INSTRUCTOR_LABEL:
                                        result = documentSnapshot.toObject(Instructor.class);
                                        break;
                                    case GymMember.GYM_MEMBER_LABEL:
                                        result = documentSnapshot.toObject(GymMember.class);
                                        break;
                                    default:
                                        Log.w(LOG_LABEL, "Invalid type label!");
                                        return;
                                }
                                onTaskComplete.onComplete(result);
                            } else {
                                Log.w(LOG_LABEL, "Document does not exist!");
                                onTaskComplete.onComplete(null);
                            }
                        } else {
                            Log.w(LOG_LABEL, "Error getting document: " + task.getException());
                            onTaskComplete.onComplete(null);
                        }
                    }
                });
    }

    public void getUserList(OnTaskComplete<UserData[]> onTaskComplete) {
        db.collection(COLLECTION_USER_INFO)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            ArrayList<UserData> users = new ArrayList<>();

                            documentLoop:
                            for(QueryDocumentSnapshot doc : task.getResult()) {

                                String type = doc.get(UserData.USERTYPE_DATABASE_LABEL, String.class);

                                UserData result;

                                switch (type) {
                                    case Instructor.INSTRUCTOR_LABEL:
                                        result = doc.toObject(Instructor.class);
                                        break;
                                    case GymMember.GYM_MEMBER_LABEL:
                                        result = doc.toObject(GymMember.class);
                                        break;
                                    default:
                                        Log.w(LOG_LABEL, "Invalid type label!");
                                        continue documentLoop;
                                }

                                users.add(result);
                            }

                            onTaskComplete.onComplete(users.toArray(new UserData[users.size()]));

                        } else {
                            Log.w(LOG_LABEL, "Error getting user document: " + task.getException());
                            onTaskComplete.onComplete(null);
                        }
                    }
                });
    }

    public void getFitClass(String ClassId, OnTaskComplete<FitClass> onTaskComplete) {
        db.collection(COLLECTION_CLASSES)
                .document(ClassId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            onTaskComplete.onComplete(task.getResult().toObject(FitClass.class));
                        } else {
                            Log.w(LOG_LABEL, "Error getting class from database: " + task.getException());
                            onTaskComplete.onComplete(null);
                        }
                    }
                });
    }

    public void getAvailableClasses(OnTaskComplete<FitClass[]> onTaskComplete) {
        db.collection(COLLECTION_CLASSES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<FitClass> classes = new ArrayList<>();

                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                classes.add(doc.toObject(FitClass.class));
                            }

                            onTaskComplete.onComplete(classes.toArray(new FitClass[classes.size()]));
                        } else {
                            Log.w(LOG_LABEL, "Error getting class list from database: " + task.getException());
                            onTaskComplete.onComplete(null);
                        }
                    }
                });
    }

    public void getFitClassType(String ClassTypeId, OnTaskComplete<FitClassType> onTaskComplete) {
        db.collection(COLLECTION_CLASS_TYPES)
                .document(ClassTypeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            onTaskComplete.onComplete(task.getResult().toObject(FitClassType.class));
                        } else {
                            Log.w(LOG_LABEL, "Error getting Class Type from database: " + task.getException());
                            onTaskComplete.onComplete(null);
                        }
                    }
                });
    }

    public void viewAllClassTypes(OnTaskComplete<FitClassType[]> onTaskComplete) {
        db.collection(COLLECTION_CLASS_TYPES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<FitClassType> classes = new ArrayList<>();

                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                classes.add(doc.toObject(FitClassType.class));
                            }

                            onTaskComplete.onComplete(classes.toArray(new FitClassType[classes.size()]));
                        } else {
                            Log.w(LOG_LABEL, "Error getting class types from database: " + task.getException());
                            onTaskComplete.onComplete(null);
                        }
                    }
                });
    }

    public void setUserData(UserData data) {
        db.collection(COLLECTION_USER_INFO)
                .document(data.getUid())
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.i(LOG_LABEL, "Added user to database!");
                        } else {
                            Log.w(LOG_LABEL, "Error adding user to database: " + task.getException());
                        }
                    }
                });
    }

    public void setFitClass(FitClass data) {
        db.collection(COLLECTION_CLASSES)
                .document(data.getUid())
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.i(LOG_LABEL, "Added Class to database!");
                        } else {
                            Log.w(LOG_LABEL, "Error adding Class to database: " + task.getException());
                        }

                    }
                });
    }

    public void setFitClassType(FitClassType data) {
        db.collection(COLLECTION_CLASSES)
                .document(data.getUid())
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.i(LOG_LABEL, "Added Class Type to database!");
                        } else {
                            Log.w(LOG_LABEL, "Error adding Class Type to database: " + task.getException());
                        }
                    }
                });
    }
}
