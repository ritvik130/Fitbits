package me.seg.fitbites.layouts.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import me.seg.fitbites.MainActivity;
import me.seg.fitbites.R;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.data.GymMember;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class MemberSchedule extends AppCompatActivity {
    FirestoreDatabase db;
    String classUid;
    GymMember gm;
    ArrayList<FitClass> classes;
    ArrayList<String> classesViewData;
    ArrayList<String> instructorNames;
    ArrayList<String> classNames;
    TextView lb;
    ListView scheduled;
    Button unenroll;
    AlertDialog confirm;
    FitClass f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_schedule);
        gm = (GymMember)AuthManager.getInstance().getCurrentUserData();
        classes = new ArrayList<>();
        classesViewData = new ArrayList<>();
        instructorNames = new ArrayList<>();
        classNames = new ArrayList<>();
        unenroll = findViewById(R.id.member_drop_button);
        scheduled = findViewById(R.id.member_scheduled_classes);
        lb = findViewById(R.id.member_select_text);

        //addTestClass();
        if(gm.getEnrolledClasses().size()==0){
            lb.setText("You have not enrolled in any classes yet.");
        }
        viewEnrolledClasses(gm, new OnTaskComplete<ArrayList<FitClass>>() {
            @Override
            public void onComplete(ArrayList<FitClass> result) {
                classes = result;
                convertToString(classes, new OnTaskComplete<ArrayList<String>>() {
                    @Override
                    public void onComplete(ArrayList<String> viewStringResult) {
                        for(int i =0; i<classes.size(); i++){
                            classesViewData.add(classNames.get(i)+" is taught by "+instructorNames.get(i)+" on "+classes.get(i).getDate()+" at "+classes.get(i).getTime());
                        }
                        display();
                    }
                });
            }
        });
        scheduled.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(classesViewData.get(0).equals("There are no scheduled classes")) classUid = null;
                else{
                    classUid = classes.get(position).getUid();
                    f = classes.get(position);
                }

            }
        });
        unenroll.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(classUid==null)Toast.makeText(MemberSchedule.this, "No class selected", Toast.LENGTH_SHORT).show();
                else{
                    confirm = new AlertDialog.Builder(MemberSchedule.this).setTitle("Confirm").setMessage("Are you sure you would like to drop this class?").setPositiveButton("Yes", null).setNegativeButton("Cancel", null).show();
                    Button yes = confirm.getButton(AlertDialog.BUTTON_POSITIVE);
                    yes.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            unenrollUser();
                        }
                    });
                }
            }
        });
    }

    public void viewEnrolledClasses(GymMember gm, OnTaskComplete<ArrayList<FitClass>> onTaskComplete) {
        int gmSize = gm.getEnrolledClasses().size();
        if (gmSize == 0) {
            return;
        }
        for (int i = 0; i < gmSize; i++) {
            db.getInstance().getFitClass(gm.getEnrolledClasses().get(i), new OnTaskComplete<FitClass>() {
                @Override
                public void onComplete(FitClass result) {
                    classes.add(result);
                    if(classes.size()==gmSize)onTaskComplete.onComplete(classes);
                }
            });
        }
    }
    public void convertToString(ArrayList<FitClass> classes, OnTaskComplete<ArrayList<String>> onTaskComplete){
        int size = classes.size();
        for(int i =0; i<size; i++){
            db.getInstance().getUserData(classes.get(i).getTeacherUID(), new OnTaskComplete<UserData>() {
                @Override
                public void onComplete(UserData result) {
                    instructorNames.add(result.getFirstName());

                    if(instructorNames.size()==classes.size()&&classNames.size()==size){
                        onTaskComplete.onComplete(null);
                    }
                }
            });

        }
        for(int i =0; i<size; i++){
            db.getInstance().getFitClassType(classes.get(i).getFitClassTypeUid(), new OnTaskComplete<FitClassType>(){
                @Override
                public void onComplete(FitClassType result) {
                    classNames.add(result.getClassName());
                    if(instructorNames.size()==classNames.size()&&classNames.size()==size){
                        onTaskComplete.onComplete(null);
                    }
                }
            });
        }
    }
    public void display(){
        if(classes == null){
            classesViewData.clear();
            classesViewData.add("There are no scheduled classes");
        }
        ArrayAdapter<String> userListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classesViewData);
        scheduled.setAdapter(userListAdapter);
    }
    public void unenrollUser(){
        gm.unenrollClass(classUid);
        f.unenrollMember(AuthManager.getInstance().getCurrentUserData());
        db.getInstance().setUserData(gm);
        Intent i = getIntent();
        finish();
        startActivity(i);
    }
}