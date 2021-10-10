package me.seg.fitbites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Member;

import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class MainActivity extends AppCompatActivity {
    Button login, signup;
    TextView user, pass;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.editTextTextPersonName);
        pass = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.button_signUp);

        final MainActivity current = this;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.getInstance().validateUser(user.getText().toString(), pass.getText().toString(), new OnTaskComplete<AuthManager.LoginResult>() {
                    @Override
                    public void onComplete(AuthManager.LoginResult result) {
                        if (result == null || !result.isSuccessful()) {
                            reEnter();
                        } else {
                            UserData u= AuthManager.getInstance().getCurrentUserData();

                            if (u instanceof Instructor){
                                Intent intent = new Intent(current, InstructorWelcome.class);
                                startActivity(intent);

                            }
                            else if (u instanceof GymMember){//TODO temp remove later
                                Intent intent = new Intent(current, InstructorWelcome.class);
                                startActivity(intent);

                            }

                        }
                    }
                });


            }
        });


        signup.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(intent);
            }
            });

        }


    @SuppressLint("SetTextI18n")
    public void reEnter() {
        user.setText("Invalid");
        pass.setText("Invalid");

    }





}