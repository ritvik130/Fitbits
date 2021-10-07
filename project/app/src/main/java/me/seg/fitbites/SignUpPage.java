package me.seg.fitbites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.regex.Matcher;

public class SignUpPage extends AppCompatActivity {
    AlertDialog error;
    Button signUpInstructor, signUpMember;
    TextView name, age, username, password, firstName, lastName, address, email;
    private final String emailValidationPattern = "[a-z0-9!#$%&\'*+/=?^_\'{|}~-]+(?:.[a-z0-9!#$%&\'*+/=?^_\'{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    SignUpPage n = new SignUpPage();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        signUpInstructor = findViewById(R.id.signUpButton);
        signUpMember = findViewById(R.id.signUpButton2);
        firstName = findViewById(R.id.editTextTextPersonName3);
        lastName = findViewById(R.id.editTextTextPersonName7);
        age = findViewById(R.id.editTextTextPersonName6);
        username = findViewById(R.id.editTextTextPersonName5);
        password = findViewById(R.id.editTextTextPassword2);
        address = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editTextTextPersonName8);


        signUpInstructor.setOnClickListener((View.OnClickListener) this);
        signUpMember.setOnClickListener((View.OnClickListener) this);

    }

    public void onClick(View v) {
        String s="";

        switch (v.getId()) {
            case R.id.signUpButton:
                s = signUpInstructor.getText().toString();
                break;

            case R.id.signUpButton2:
                s = signUpMember.getText().toString();
                break;
        }
        final String s2=s;
        final SignUpPage current=this;

        if (validateEmail() && checkInfoFilled() && checkAge()) {

            AuthManager.getInstance().createUser(username.getText().toString(), password.getText().toString(), new OnTaskComplete<AuthManager.LoginResult>() {
                @Override
                public void onComplete(AuthManager.LoginResult authResult) {
                    if (authResult == null) {
                        //notify user to re-enter info
                        reEnter();

                    } else {
                        UserData userData = new UserData(authResult.getUserData(), firstName.getText().toString(),
                                lastName.getText().toString(), username.getText().toString(), address.getText().toString(),
                                age.getText().toString(), password.getText().toString(), email.getText().toString());

                        FirestoreDatabase.getInstance().setUserData(userData);
                        AuthManager.getInstance().setCurrentLogInData(userData);

                        if (s2.equals("Sign up for Instructor!")) {
                            Intent intent = new Intent(current, InstructorWelcome.class);
                            startActivity(intent);


                        } else if (s2.equals("Sign up for Member!")) {//TODO temp send to same screen
                            Intent intent = new Intent(current, InstructorWelcome.class);
                            startActivity(intent);

                        }

                    }
                }

            });
        } else {
            reEnter();
        }


    }


    @SuppressLint("SetTextI18n")
    public void reEnter() {
        firstName.setText("Try Again");
        lastName.setText("Try Again");
        age.setText("Try Again");
        username.setText("Try Again");
        password.setText("Try Again");
        address.setText("Try Again");
        email.setText("Try Again");

    }

    public boolean validateEmail() {
        String emailTested = email.getText().toString().trim();

        boolean isValid = false;

        Pattern p = Pattern.compile(emailValidationPattern);
        Matcher m = p.matcher(emailTested);
        isValid = m.find();

        return isValid;
    }


    private boolean checkInfoFilled() {
        if (username.getText().length() < 1 || name.getText().length() < 1 || age.getText().length() < 1 || password.getText().length() < 1) {
            AlertDialog.Builder nullParams = new AlertDialog.Builder(SignUpPage.this);
            nullParams.setCancelable(true);
            nullParams.setTitle("*Error*");
            nullParams.setMessage("All information must be filled!");
            error = nullParams.create();
            error.show();
            return false;
        } else {
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

}





