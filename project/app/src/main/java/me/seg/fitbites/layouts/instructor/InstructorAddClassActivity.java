package me.seg.fitbites.layouts.instructor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import me.seg.fitbites.R;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import me.seg.fitbites.data.Difficulty;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.admin.AdminDeleteClassActivity;
import me.seg.fitbites.layouts.admin.AdminWelcomeActivity;

public class InstructorAddClassActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addCLass,backButton, dateButton,timeButton, searchButton;
    private TextView dateTextView, timeTextView,classCapacity,description;
    private EditText classText;
    private Difficulty difficulty;
    private String difficultySelection;
    private int capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_class);

        radioGroup= findViewById(R.id.radioGroup);
        dateButton= findViewById(R.id.addDateButton);
        timeButton= findViewById(R.id.addTimeButton);
        dateTextView=findViewById(R.id.dateTextView);
        backButton=findViewById(R.id.AddClassBackBtn);
        searchButton=findViewById(R.id.searchButton1);
        classText=findViewById(R.id.classText);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        backButton.setOnClickListener((View.OnClickListener) this);
        final InstructorAddClassActivity current = this;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(current, InstructorWelcomeActivity.class);
                startActivity(intent);
            }
        });

        addCLass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddClassButton();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                FitClassType.searchClass(classText.getText().toString(), new OnTaskComplete<FitClassType[]>() {
                    @Override
                    public void onComplete(FitClassType[] result) {

                        placeIntoResults(result);
                    }
                });

            }

        });
    }



    // allows instructor to select the time
    private void handleTimeButton(){
        Calendar calendar=Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute= calendar.get(Calendar.MINUTE);
        boolean is24HourFormat= DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar1= Calendar.getInstance();
                calendar1.set(Calendar.HOUR,hour);
                calendar1.set(Calendar.MINUTE,minute);

                CharSequence timeCharSequence = DateFormat.format("hh:mm a",calendar1);
                timeTextView.setText(timeCharSequence);

            }
        }, hour, minute, is24HourFormat);
        timePickerDialog.show();
    }
    // allows instructor to select the date
    private void handleDateButton(){
        Calendar calendar=Calendar.getInstance();
        int date= calendar.get(Calendar.DATE);
        int year= calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {

                Calendar calendar1= Calendar.getInstance();
                calendar1.set(Calendar.DATE,date);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.YEAR,year);

                CharSequence dateCharSequence= DateFormat.format("MMM d, yyyy",calendar1);
                dateTextView.setText(dateCharSequence);
            }
        },year, month, date);
        datePickerDialog.show();
    }

    private void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);
        Toast.makeText(this, radioButton.getText()+" selected", Toast.LENGTH_SHORT).show();
        difficultySelection=radioButton.getText().toString();
        if(difficultySelection.equals("Beginner")){
            difficulty=difficulty.BEGINNER;
        }else if (difficultySelection.equals("Intermediate")){
            difficulty=difficulty.INTERMEDIATE;
        }else{
            difficulty=difficulty.EXPERIENCED;
        }
    }

    private void handleAddClassButton(){
        capacity=Integer.parseInt(classCapacity.getText().toString());
    }

    private void placeIntoResults(FitClassType[] r){
        LinearLayout layout = (LinearLayout)findViewById(R.id.searchresultslayout);
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

                    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (DialogInterface.BUTTON_POSITIVE == which) {
                                FirestoreDatabase.getInstance().deleteFitClassType(c);
                                Intent intent = new Intent(InstructorAddClassActivity.this, AdminWelcomeActivity.class);
                                startActivity(intent);
                            }

                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(InstructorAddClassActivity.this);
                    builder.setMessage("Are you sure you want to select this class?")
                            .setPositiveButton("Yes",dialogListener)
                            .setNegativeButton("No", dialogListener);
                    builder.show();
                }
            });
            layout.addView(button, layoutP);

        }

        if(!addedAtLeastOne) {
            TextView v = new TextView(this);
            v.setText("No Classes Found");
            layout.addView(v, layoutP);
        }

    }
}