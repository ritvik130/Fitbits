package me.seg.fitbites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import me.seg.fitbites.data.GymMember;
import me.seg.fitbites.data.Instructor;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.member.MemberWelcomeActivity;
import me.seg.fitbites.layouts.instructor.InstructorWelcomeActivity;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MainCreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog error;
    private Button signUpInstructor, signUpMember;
    private EditText age, username, password, firstName, lastName, address, email;
    private final String emailValidationPattern = "[a-z0-9!#$%&\'*+/=?^_\'{|}~-]+(?:.[a-z0-9!#$%&\'*+/=?^_\'{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_createAccount);

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

        final MainCreateAccountActivity current=this;

        if (validateEmail() && checkInfoFilled() && checkAge()) {

            AuthManager.getInstance().createUser(email.getText().toString().trim(), password.getText().toString(), new OnTaskComplete<AuthManager.LoginResult>() {
                @Override
                public void onComplete(AuthManager.LoginResult authResult) {
                    if (authResult == null || !authResult.isSuccessful()) {
                        //notify user to re-enter info
                        reEnter();

                    } else {
                        UserData userData = null;

                        if(v.getId() == R.id.signUpButton) {
                            //is instructor
                            userData = new Instructor(authResult.getUserData(), firstName.getText().toString(),
                                lastName.getText().toString(), username.getText().toString(), address.getText().toString(),
                                age.getText().toString(), password.getText().toString(), email.getText().toString());
                        } else {
                            //is a member
                            userData = new GymMember(authResult.getUserData(), firstName.getText().toString(),
                                    lastName.getText().toString(), username.getText().toString(), address.getText().toString(),
                                    age.getText().toString(), password.getText().toString(), email.getText().toString());
                        }

                        FirestoreDatabase.getInstance().setUserData(userData);
                        AuthManager.getInstance().setCurrentLogInData(userData);

                        if (v.getId() == R.id.signUpButton) {
                            //is instructor
                            Intent intent = new Intent(current, InstructorWelcomeActivity.class);
                            startActivity(intent);


                        } else {
                            //is member
                            Intent intent = new Intent(current, MemberWelcomeActivity.class);
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
        AlertDialog.Builder badinput = new AlertDialog.Builder(MainCreateAccountActivity.this);
        badinput.setTitle("*Error*");
        badinput.setMessage("There was a problem with the information you put in, did you check that your password has at least 6 characters? A account already exists? Email address does not exist?");

        AlertDialog e = badinput.create();
        e.show();
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
        if (username.getText().length() < 1 || age.getText().length() < 1 || password.getText().length() < 1 ||
            firstName.getText().length() < 1 || lastName.getText().length() < 1 || email.getText().length() < 1
        ) {
            AlertDialog.Builder nullParams = new AlertDialog.Builder(MainCreateAccountActivity.this);
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
            AlertDialog.Builder ageNotNum = new AlertDialog.Builder(MainCreateAccountActivity.this);
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





