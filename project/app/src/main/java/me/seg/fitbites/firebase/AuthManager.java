package me.seg.fitbites.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.auth.User;

import me.seg.fitbites.Admin;
import me.seg.fitbites.UserData;

public class AuthManager {

    private static AuthManager instance;
    public static final String LOG_TAG = "AuthManager";

    private FirebaseAuth authInstance;
    private String currentLogIn; //the uid of the currently logged in user
    private UserData currentLogInData;

    public static AuthManager getInstance() {
        if(instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public AuthManager() {
        if(instance != null && instance.authInstance.getCurrentUser() != null) {
            authInstance = instance.authInstance;
        } else {
            authInstance = FirebaseAuth.getInstance();
        }
        instance = this;
    }

    public void validateUser(String email, String password, OnTaskComplete<LoginResult> onComplete) {
        //sign in user
        if(email.equalsIgnoreCase("admin") && password.equals("admin123")) {
            currentLogIn = "admin";
            currentLogInData = new Admin();
            onComplete.onComplete(new LoginResult(true, "Admin"));
            return;
        }
        //created using example from Official Documentation:
        //https://firebase.google.com/docs/auth/android/start#sign_up_new_users
        authInstance.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.i(LOG_TAG, "User: " + authInstance.getCurrentUser().getEmail() +
                            " logged in successfully! Id: " + authInstance.getCurrentUser().getUid());
                    currentLogIn = task.getResult().getUser().getUid();
                    FirestoreDatabase.getInstance().getUserData(currentLogIn, new OnTaskComplete<UserData>() {
                        @Override
                        public void onComplete(UserData result) {
                            currentLogInData = result;
                            onComplete.onComplete(new LoginResult(true, task.getResult().getUser().getUid()));
                        }
                    });

                } else {
                    Log.i(LOG_TAG, "User: " + email + " failed to login");
                    onComplete.onComplete(new LoginResult(false, null));
                }
            }
        });
    }

    public void createUser(String email, String password, OnTaskComplete<LoginResult> onComplete) {
        //create a new account
        //created using example from Official Documentation:
        //https://firebase.google.com/docs/auth/android/start#sign_in_existing_users
            authInstance.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "User: " + authInstance.getCurrentUser().getEmail() +
                                " created account successfully! Id: " + authInstance.getCurrentUser().getUid());
                        currentLogIn = task.getResult().getUser().getUid();

                        onComplete.onComplete(new LoginResult(true, task.getResult().getUser().getUid()));
                    } else {
                        Log.i(LOG_TAG, "User: " + email + " failed to create account, reason: " + task.getException());
                        onComplete.onComplete(new LoginResult(false, null));
                    }
                }
            });

    }

    public void deleteAccount(UserData user) {
        if(currentLogInData != null && currentLogInData instanceof Admin) {
            //delete from database
            Log.i(LOG_TAG, "Removing user from database");
            FirestoreDatabase.getInstance().deleteUserData(user);
            authInstance.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                task.getResult().getUser().delete();
                                Log.i(LOG_TAG, "Remove successful");
                            } else {
                                Log.w(LOG_TAG, "Something went wrong with deleting");
                            }
                            authInstance.signOut();
                        }
                    });
        }
    }

    public void signoutUser() {
        authInstance.signOut();
        currentLogInData = null;
        currentLogIn = null;
    }

    public void setCurrentLogInData(UserData d) {
        currentLogInData = d;
    }

    public String getCurrentUser() { return currentLogIn; }
    public UserData getCurrentUserData() { return currentLogInData; }

    public class LoginResult {
        private boolean success;
        private String useruid;

        public LoginResult(boolean s, String ud) {
            success = s;
            useruid = ud;
        }

        public boolean isSuccessful() {
            return success;
        }
        public String getUserData() { return useruid; }
    }

}
