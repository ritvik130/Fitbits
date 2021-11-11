package me.seg.fitbites.layouts.instructor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.seg.fitbites.R;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.admin.AdminEditClassActivity;
import me.seg.fitbites.layouts.admin.AdminEditClass_SearchActivity;

public class InstructorCancelClassActivity extends AppCompatActivity {

    private Button removeBTN, searchBTN, backBTN;
    private EditText RemoveClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_cancel_class);

        RemoveClass = findViewById(R.id.classtext);
        removeBTN = findViewById(R.id.searchButton3);
        searchBTN = findViewById(R.id.searchButton);
        backBTN = findViewById(R.id.search_class_back_btn);



    }
        backBTN.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            startActivity(i);
        }
    });


        //FirestoreDatabase.getInstance().deleteFitClass((FitClass) RemoveClass.getText());
  searchBTN.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        FitClass.searchClass(RemoveClass.getText().toString(), new OnTaskComplete<FitClass[]>() {
            @Override
            public void onComplete(FitClass[] result) {

                //output classes in array into search box


                //resultList.toArray(new FitClass[resultList.size()]))
                placeIntoResults(result);
            }
        });


    }

    });


    public void placeIntoResults(FitClass[] r){
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout2);
        LinearLayout.LayoutParams layoutP= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layout.removeAllViews();
        boolean addedAtLeastOne = false;

        for (FitClass c: r){
            addedAtLeastOne = true;
            Button button= new Button(this);
            button.setText(c.getTime());
            button.setText(c.getDifficulty());
            button.setText(c.getDate());
            button.setText(c.getCapacity());
            FirestoreDatabase.getInstance().getFitClassType(c.getUid(), new OnTaskComplete<FitClassType>() {
                @Override
                public void onComplete(FitClassType result) {
                    button.setText(result.getClassName());
                   ;
                }
            });


            //
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //click remove
                    //delete class from database






                    Intent intent = new Intent(AdminEditClass_SearchActivity.this, AdminEditClassActivity.class);
                    intent.putExtra("uid",c.getUid());
                    startActivity(intent);
                }
            });
            layout.addView(button, layoutP);
        }




    }

}



