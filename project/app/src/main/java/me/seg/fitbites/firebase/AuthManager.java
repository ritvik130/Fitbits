package me.seg.fitbites.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class AuthManager {

    public static AuthManager instance;
    public static final String LOG_TAG = "AuthManager";

    private FirebaseAuth authInstance;

    public AuthManager() {
        instance = this;
        authInstance = FirebaseAuth.getInstance();
    }

    public void validateUser(String email, String password, OnTaskComplete<LoginResult> onComplete) {
        //sign in user
        if(email.equalsIgnoreCase("admin") && email.equals("admin123")) {
            onComplete.onComplete(new LoginResult(true));
        }
        authInstance.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.i(LOG_TAG, "User: " + authInstance.getCurrentUser().getEmail() +
                            " created account successfully! Id: " + authInstance.getCurrentUser().getUid());
                } else {
                    Log.i(LOG_TAG, "User: " + email + " failed to create account");
                }
                onComplete.onComplete(new LoginResult(task.isSuccessful()));
            }
        });
    }

    public void createUser(String email, String password, OnTaskComplete<LoginResult> onComplete) {
        //create a new account
        authInstance.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //TODO add account type logic
                if(task.isSuccessful()) {
                    Log.i(LOG_TAG, "User: " + authInstance.getCurrentUser().getEmail() +
                            " created account successfully! Id: " + authInstance.getCurrentUser().getUid());
                } else {
                    Log.i(LOG_TAG, "User: " + email + " failed to create account");
                }
                onComplete.onComplete(new LoginResult(task.isSuccessful()));
            }
        });
    }

    //TODO: Setup User class with result
    public class LoginResult {
        private boolean success;

        public LoginResult(boolean s) {
            success = s;
        }

        public boolean isSuccessful() {
            return success;
        }
    }

}
