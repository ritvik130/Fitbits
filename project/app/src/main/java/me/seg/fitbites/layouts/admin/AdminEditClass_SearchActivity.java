package me.seg.fitbites.layouts.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.seg.fitbites.R;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class AdminEditClass_SearchActivity extends AppCompatActivity {
    private Button searchBTN, bkBtn;
    private TextView className;
    private FirestoreDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editclass_search);

        searchBTN = findViewById(R.id.searchButton);
        className = findViewById(R.id.classtext);
        bkBtn = findViewById(R.id.search_class_back_btn);

        //final ChangeClassScreen current = this;

        db.getInstance().viewAllClassTypes(new OnTaskComplete<FitClassType[]>() {
            @Override
            public void onComplete(FitClassType[] result) {
                placeIntoResults(result);
            }
        });


        searchBTN.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                FitClassType.searchClass(className.getText().toString(), new OnTaskComplete<FitClassType[]>() {
                    @Override
                    public void onComplete(FitClassType[] result) {
                        placeIntoResults(result);
                    }
                });


            }

        });


    }


    public void placeIntoResults(FitClassType[] r){
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout1);
        LinearLayout.LayoutParams layoutP= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layout.removeAllViews();
        boolean addedAtLeastOne = false;

        for (FitClassType c: r){
            addedAtLeastOne = true;
            Button button= new Button(this);
            button.setText(c.getClassName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminEditClass_SearchActivity.this, AdminEditClassActivity.class);
                    intent.putExtra("uid",c.getUid());
                    startActivity(intent);
                }
            });
            layout.addView(button, layoutP);
        }

        if(!addedAtLeastOne) {
            TextView v = new TextView(this);
            v.setText("No Classes Found");
            layout.addView(v, layoutP);
        }

        bkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminEditClass_SearchActivity.this, AdminClassOptionsActivity.class);
                startActivity(i);
            }
        });


    }

}
