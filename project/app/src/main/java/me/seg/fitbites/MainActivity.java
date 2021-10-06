package me.seg.fitbites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.seg.fitbites.firebase.FirestoreDatabase;

public class MainActivity extends AppCompatActivity {
    Button login, signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testbestTestEver();

        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //----------------attempt to login code-----------------------
            }
        });
    }
    public void openSignUp(View v){
        Intent launchSignUp = new Intent(getApplicationContext(), SignUpPage.class);
        startActivity(launchSignUp);
    }
    public void testbestTestEver() {
        FirestoreDatabase fdb = new FirestoreDatabase();

        //fdb.setUserData(new GymMember("IdThingyIdk2", "Poggers2", "Champ2", "blah", "123 EArth", 20));

//        fdb.getUserData("IdThingyIdk", new OnTaskComplete<UserData>() {
//            @Override
//            public void onComplete(UserData result) {
//                if(result != null) {
//                    Log.i("Tester", result.address);
//                    Log.i("Tester", result.firstName);
//                    Log.i("Tester", result.label);
//                    Log.i("Tester", result.lastName);
//                    Log.i("Tester", result.userName);
//                    Log.i("Tester", String.valueOf(result.age));
//                    Log.i("Tester", result.uid);
//                } else {
//                    Log.i("Tester", "FAILED");
//                }
//            }
//        });

//        fdb.getUserList(new OnTaskComplete<UserData[]>() {
//            @Override
//            public void onComplete(UserData[] result) {
//                if(result != null) {
//                    for (UserData ud : result) {
//                        if (ud != null) {
//                            Log.i("Tester", ud.address);
//                            Log.i("Tester", ud.firstName);
//                            Log.i("Tester", ud.label);
//                            Log.i("Tester", ud.lastName);
//                            Log.i("Tester", ud.userName);
//                            Log.i("Tester", String.valueOf(ud.age));
//                            Log.i("Tester", ud.uid);
//                        } else {
//                            Log.i("Tester", "FAILED");
//                        }
//                    }
//                } else {
//                    Log.i("Tester", "You idiot");
//                }
//            }
//        });

    }



}