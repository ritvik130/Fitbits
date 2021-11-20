package me.seg.fitbites.layouts.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import me.seg.fitbites.R;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.admin.AdminClassOptionsActivity;

public class InstructorEditClass_SearchActivity extends AppCompatActivity {
    private Button searchBTN, bkBtn;
    private TextView className;
    private FirestoreDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_editclass_search);

        className = findViewById(R.id.classText);
        bkBtn = findViewById(R.id.search_class_back_btn);

        //final ChangeClassScreen current = this;

        db.getInstance().getAvailableClasses(new OnTaskComplete<FitClass[]>() {
            @Override
            public void onComplete(FitClass[] result) {

                UserData cur = AuthManager.getInstance().getCurrentUserData();

                ArrayList<FitClass> classes = new ArrayList<>();

                for(FitClass f : result) {
                    if(f.getTeacherUID().equals(cur.getUid())) {
                        classes.add(f);
                    }
                }

                placeIntoResults(classes.toArray(new FitClass[classes.size()]));
            }
        });

        bkBtn = (Button) findViewById(R.id.search_class_back_btn);
        bkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstructorEditClass_SearchActivity.this, InstructorWelcomeActivity.class);
                startActivity(i);
            }
        });
    }


    public void placeIntoResults(FitClass[] r){
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout1);
        LinearLayout.LayoutParams layoutP= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layout.removeAllViews();

        FirestoreDatabase.getInstance().viewAllClassTypes(new OnTaskComplete<FitClassType[]>() {
            @Override
            public void onComplete(FitClassType[] result) {

                HashMap<String, FitClassType> types = new HashMap<>();
                for(FitClassType f : result) {
                    types.put(f.getUid(), f);
                }

                boolean addedAtLeastOne = false;

                for (FitClass c: r){
                    addedAtLeastOne = true;
                    Button button= new Button(InstructorEditClass_SearchActivity.this);
                    button.setText(types.get(c.getFitClassTypeUid()).getClassName() + " - " + c.getDateObj().toStringShort());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(InstructorEditClass_SearchActivity.this, InstructorEditClassActivity.class);
                            intent.putExtra("classuid",c.getUid());
                            startActivity(intent);
                        }
                    });
                    layout.addView(button, layoutP);
                }

                if(!addedAtLeastOne) {
                    TextView v = new TextView(InstructorEditClass_SearchActivity.this);
                    v.setText("You teach no classes");
                    layout.addView(v, layoutP);
                }
            }
        });
    }

}
