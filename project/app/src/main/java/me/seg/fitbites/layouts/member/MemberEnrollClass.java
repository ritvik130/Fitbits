package me.seg.fitbites.layouts.member;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import me.seg.fitbites.MainCreateAccountActivity;
import me.seg.fitbites.R;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.data.GymMember;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.admin.AdminClassOptionsActivity;
import me.seg.fitbites.layouts.admin.AdminEditClassActivity;
import me.seg.fitbites.layouts.admin.AdminEditClass_SearchActivity;
import me.seg.fitbites.layouts.instructor.InstructorAddClass_SearchActivity;
import me.seg.fitbites.layouts.instructor.InstructorEditClass_SearchActivity;
import me.seg.fitbites.layouts.instructor.InstructorWelcomeActivity;

public class MemberEnrollClass extends AppCompatActivity {

    private Button searchBTN, bkBtn, enrollBTN;
    private EditText className, day;
    private FirestoreDatabase db;
    String Day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_enroll_class);

        className = findViewById(R.id.editTextTextPersonName13);
        day = findViewById(R.id.editTextTextPersonName12);



        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (day.getText().toString().toUpperCase().equals("MONDAY")) { Day = "MONDAY"; }
                    else if (day.getText().toString().toUpperCase().equals("TUESDAY")) { Day = "TUESDAY"; }
                       else if (day.getText().toString().toUpperCase().equals("WEDNESDAY")) { Day = "WEDNESDAY"; }
                           else if (day.getText().toString().toUpperCase().equals("THURSDAY")) { Day = "THURSDAY"; }
                           else if (day.getText().toString().toUpperCase().equals("FRIDAY")) { Day = "FRIDAY"; }
                           else  if (day.getText().toString().toUpperCase().equals("SATURDAY")) { Day = "SATURDAY"; }
                           else    if (day.getText().toString().toUpperCase().equals("SUNDAY")) { Day="SUNDAY"; }

                else{

                    reEnter();
                    return;

                }

                FitClass.searchClass(className.getText().toString(), new OnTaskComplete<FitClass[]>() {
                    @Override
                    public void onComplete(FitClass[] result) {
                        placeIntoResults(result);
                    }
                });


            }

        });


    }

    public void placeIntoResults(FitClass[] r) {//put options in the box

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout2);
        LinearLayout.LayoutParams layoutP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layout.removeAllViews();
        boolean addedAtLeastOne = false;

        for (FitClass c : r) {

            if (c.getDate().equals(Day)) {

                addedAtLeastOne = true;
                Button button = new Button(this);
                button.setText("Capacity: " + c.getCapacity() + "Difficulty: " + c.getDifficulty());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        enrollBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                GymMember user = (GymMember) AuthManager.getInstance().getCurrentUserData();
                                user.checkClassCollision(c, new OnTaskComplete<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean result) {
                                        if (!result) {
                                            user.enrollClass(c);
                                            c.enrollMember(user);
                                        }
                                    }
                                });


                            }
                        });

                    }
                });
                layout.addView(button, layoutP);
            }
            if (!addedAtLeastOne) {
                TextView v = new TextView(this);
                v.setText("No Classes Found");
                layout.addView(v, layoutP);
            }

        bkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MemberEnrollClass.this, MemberWelcomeActivity.class);
                startActivity(i);
            }
        });

        }


    }

    public void reEnter() {
        AlertDialog.Builder badinput = new AlertDialog.Builder(MemberEnrollClass.this);
        badinput.setTitle("*Error*");
        badinput.setMessage("INVALID DAY");
        AlertDialog e = badinput.create();
        e.show();
    }
}





