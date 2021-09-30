package me.seg.fitbites.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.auth.User;

import me.seg.fitbites.UserData;

public class AuthManager {

    public static AuthManager instance;
    public static final String LOG_TAG = "AuthManager";

    private FirebaseAuth authInstance;
    private UserData currentLogIn;

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
        if(email.equalsIgnoreCase("admin") && email.equals("admin123")) {
            onComplete.onComplete(new LoginResult(true, "Admin"));
        }
        //created using example from Official Documentation:
        //https://firebase.google.com/docs/auth/android/start#sign_up_new_users
        authInstance.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.i(LOG_TAG, "User: " + authInstance.getCurrentUser().getEmail() +
                            " logged in successfully! Id: " + authInstance.getCurrentUser().getUid());

                    onComplete.onComplete(new LoginResult(true, task.getResult().getUser().getUid()));

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
                if(task.isSuccessful()) {
                    Log.i(LOG_TAG, "User: " + authInstance.getCurrentUser().getEmail() +
                            " created account successfully! Id: " + authInstance.getCurrentUser().getUid());
                    onComplete.onComplete(new LoginResult(true, task.getResult().getUser().getUid()));
                } else {
                    Log.i(LOG_TAG, "User: " + email + " failed to create account");
                    onComplete.onComplete(new LoginResult(false, null));
                }
            }
        });
    }

    public void signoutUser() {
        authInstance.signOut();
    }

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
