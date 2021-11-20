package me.seg.fitbites.layouts.instructor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.seg.fitbites.MainCreateAccountActivity;
import me.seg.fitbites.R;
import me.seg.fitbites.data.Days;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.admin.AdminClassOptionsActivity;
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

        final InstructorCancelClassActivity current=this;

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent i = new Intent(current,);//TODO ?
               // startActivity(i);
            }
        });


        searchBTN.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                FitClass.searchClass(RemoveClass.getText().toString(), new OnTaskComplete<FitClass[]>() {
                    @Override
                    public void onComplete(FitClass[] result) {

                        placeIntoResults(result);
                    }
                });


            }

        });

    }

}
