package me.seg.fitbites;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import me.seg.fitbites.R;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import java.util.regex.Pattern;

public class SignUpPage extends AppCompatActivity {
    AlertDialog error;
    Button signUp;
    TextView name, age, username, password, firstName, lastName, address, email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        signUp = findViewById(R.id.signUpButton1);
        firstName = findViewById(R.id.editTextTextPersonName3);
        lastName = findViewById(R.id.editTextTextPersonName7);
        age = findViewById(R.id.editTextTextPersonName6);
        username = findViewById(R.id.editTextTextPersonName5);
        password = findViewById(R.id.editTextTextPassword2);
        address = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editTextTextPersonName8);



        signUp.setOnClickListener(new View.OnClickListener(){


            public void onClick(View v){


                if  (validateEmail() && checkInfoFilled() && checkAge()) {

                    AuthManager.getInstance().createUser(username.getText().toString(), password.getText().toString(), new OnTaskComplete<AuthManager.LoginResult>() {
                        @Override
                        public void onComplete(AuthManager.LoginResult authResult) {
                            UserData userData = new UserData(authResult.getUserData(),firstName.getText.toString(),
                                    lastName.getText.toString(), username.getText().toString(),address.getText.toString(),
                                    age.getText.toString(), password.getText.toString(), email.getText.toString());
                            FirestoreDatabase.getInstance().setUserData(userData);
                            AuthManager.getInstance().setCurrentLogInData(userData);


                            if (authResult==null){
                                //notify user
                            }


                        }
                    });
                }



            }

            private boolean validateEmail() {
                String e = email.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    username.setError("Not valid email address");
                    return false;
                } else {
                    return true;
                }
            }

            private boolean checkInfoFilled(){
                if(username.getText().length()<1||name.getText().length()<1||age.getText().length()<1||password.getText().length()<1){
                    AlertDialog.Builder nullParams = new AlertDialog.Builder(SignUpPage.this);
                    nullParams.setCancelable(true);
                    nullParams.setTitle("*Error*");
                    nullParams.setMessage("All information must be filled!");
                    error = nullParams.create();
                    error.show();
                    return false;
                }
                else{
                    return true;
                }

            }
            private boolean checkAge() {
                try {
                    Integer.parseInt(age.getText().toString());
                } catch (Exception e) {
                    AlertDialog.Builder ageNotNum = new AlertDialog.Builder(SignUpPage.this);
                    ageNotNum.setCancelable(true);
                    ageNotNum.setTitle("*Error*");
                    ageNotNum.setMessage("Age must be a whole number!");
                    error = ageNotNum.create();
                    error.show();
                    return false;
                }
                return true;

            }

        });

    }



}

