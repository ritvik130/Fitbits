package me.seg.fitbites.layouts.instructor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import me.seg.fitbites.R;
import me.seg.fitbites.data.ClassInfoDisplay;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnObjectFilled;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.admin.AdminAddClassActivity;
import me.seg.fitbites.layouts.admin.AdminWelcomeActivity;

public class instructor_search_class extends AppCompatActivity {
    private ToggleButton toggle;
    private Button search;
    private EditText currSearch;
    private ListView userList;
    private boolean searchClass;
    private FirestoreDatabase db;
    private ClassInfoDisplay cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_search_class);
        toggle = findViewById(R.id.inst_search_toggle);
        search = findViewById(R.id.inst_search_btn);
        currSearch = findViewById(R.id.inst_search_by_toggle);
        userList = findViewById(R.id.inst_search_listOfUsers);
        toggle.setChecked(false);
        searchClass = false;

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchClass = true;
                    currSearch.setHint(getResources().getString(R.string.Inst_class_toggle));
                } else {
                    searchClass = false;
                    currSearch.setHint(getResources().getString(R.string.Inst_name_toggle));
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchClass){
                    FitClass.searchClass(currSearch.getText().toString(), new OnTaskComplete<FitClass[]>() {
                        @Override
                        public void onComplete(FitClass[] result) {
                            saveResults(result, new OnTaskComplete<ClassInfoDisplay>() {
                                @Override
                                public void onComplete(ClassInfoDisplay result) {
                                    cd = result;
                                    displayResults(cd);
                                }
                            });
                        }
                    });
                }else{
                    FitClass.searchClassByTeacher(currSearch.getText().toString(), new OnTaskComplete<FitClass[]>() {
                        @Override
                        public void onComplete(FitClass[] result) {
                            saveResults(result, new OnTaskComplete<ClassInfoDisplay>() {
                                @Override
                                public void onComplete(ClassInfoDisplay result) {
                                    cd = result;
                                    displayResults(cd);
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    private void saveResults(FitClass[] result, OnTaskComplete<ClassInfoDisplay> onTaskComplete) {
        ArrayList<String> users = new ArrayList<String>();
        ArrayList<String> className = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        if (result == null){
            onTaskComplete.onComplete(null);
            return ;
        }
        if(result.length==0){
            onTaskComplete.onComplete(null);
            return;
        }
        ClassInfoDisplay cI = new ClassInfoDisplay();
        for(FitClass f:result){
            db.getInstance().getUserData(f.getTeacherUID()+"", new OnTaskComplete<UserData>() {
                @Override
                public void onComplete(UserData result) {
                    users.add(result.getLastName()+", "+result.getFirstName());
                    cI.setUsers(users);
                    if(users.size()==date.size()&&className.size()==date.size()){
                        onTaskComplete.onComplete(cI);
                    }
                }
            });
            db.getInstance().getFitClassType(f.getFitClassTypeUid() + "", new OnTaskComplete<FitClassType>() {
                @Override
                public void onComplete(FitClassType result) {
                    className.add(result.getClassName());
                    cI.setClassName(className);
                    if(users.size()==date.size()&&className.size()==date.size()){
                        onTaskComplete.onComplete(cI);
                    }
                }
            });
            date.add(f.getDateObj().toString());
            cI.setDate(date);
        }
        Log.e("test", date.size()+"");
        Log.e("test", className.size()+"");

    }
    //return list of users in list view
    private void displayResults(ClassInfoDisplay cd){
        ArrayList<String> msg = new ArrayList<String>();
        if(cd==null){
            msg.add("No results found.");
        }else{
            ArrayList<String> users = cd.getUsers();
            ArrayList<String> className = cd.getClassName();
            ArrayList<String> date = cd.getDate();
            for(int i =0; i<users.size(); i++){
                msg.add("Instructor: "+users.get(i)+" is teaching "+className.get(i)+" on "+date.get(i));
            }
        }
        ArrayAdapter<String> userListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msg);
        userList.setAdapter(userListAdapter);
    }

}