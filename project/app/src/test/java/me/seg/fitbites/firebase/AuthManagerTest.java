package me.seg.fitbites.firebase;

import android.util.Log;

import junit.framework.TestCase;

public class AuthManagerTest extends TestCase {

    public void testbestTestEver() {
        AuthManager manager = new AuthManager();

        manager.createUser("kylejacob78@gmail.com", "helloWorld", new OnTaskComplete<AuthManager.LoginResult>() {
            @Override
            public void onComplete(AuthManager.LoginResult result) {
                assertTrue(result.isSuccessful());
                Log.i("Tester", result.getUserData());
            }
        });

    }

}