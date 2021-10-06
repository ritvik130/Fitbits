package me.seg.fitbites;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import me.seg.fitbites.R;

public class SignUpPage extends AppCompatActivity {
    AlertDialog error;
    Button signUp;
    TextView name, age, username, password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        signUp = findViewById(R.id.signUpButton1);
        name = findViewById(R.id.editTextTextPersonName3);
        age = findViewById(R.id.editTextTextPersonName2);
        username = findViewById(R.id.editTextTextPersonName5);
        password = findViewById(R.id.editTextTextPassword2);

        signUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(username.getText().length()<1||name.getText().length()<1||age.getText().length()<1||password.getText().length()<1){
                    AlertDialog.Builder nullParams = new AlertDialog.Builder(SignUpPage.this);
                    nullParams.setCancelable(true);
                    nullParams.setTitle("*Error*");
                    nullParams.setMessage("All information must be filled!");
                    error = nullParams.create();
                    error.show();
                }
                try{
                    Integer.parseInt(age.getText().toString());
                }catch(Exception e){
                    AlertDialog.Builder ageNotNum = new AlertDialog.Builder(SignUpPage.this);
                    ageNotNum.setCancelable(true);
                    ageNotNum.setTitle("*Error*");
                    ageNotNum.setMessage("Age must be a whole number!");
                    error = ageNotNum.create();
                    error.show();
                }

                //----------signUp for new account code here------------------
            }
        });

    }
}
