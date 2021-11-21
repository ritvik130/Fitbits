package me.seg.fitbites.layouts.instructor;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import me.seg.fitbites.R;
import me.seg.fitbites.data.Days;
import me.seg.fitbites.data.Difficulty;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class InstructorEditClassActivity extends AppCompatActivity{

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addCLass,backButton, dateButton, picktime, picktimeend;
    private TextView dateTextView, durationTextView,classCapacity;
    private EditText classText, capText;
    private Difficulty difficulty = Difficulty.BEGINNER;
    private String difficultySelection;
    private int capacity = 5, time = 420, timeEnd = 480;
    private Days day = Days.SUNDAY;
    private ArrayList<FitClass> resultList= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_edit_class);

        radioGroup= findViewById(R.id.radioGroup);
        dateButton= findViewById(R.id.addDateButton);
        durationTextView=findViewById(R.id.DurationTextView);
        dateTextView=findViewById(R.id.dateTextView);
        backButton=findViewById(R.id.AddClassBackBtn);
        classText=findViewById(R.id.classText);
        addCLass=findViewById(R.id.FinalizeAddClassBtn);
        classCapacity=findViewById(R.id.capacityTextView);
        picktime = findViewById(R.id.picktime);
        picktimeend = findViewById(R.id.picktimeend);
        capText = findViewById(R.id.capacityEdit);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(InstructorEditClassActivity.this,InstructorWelcomeActivity.class);
                startActivity(i);
            }
        });

        addCLass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddClassButton();
            }
        });

        picktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(InstructorEditClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = FitClass.convertTime(hourOfDay, minute);

                        if(timeEnd < time) {
                            timeEnd = time + 60;
                        }
                    }
                }, time/60, time % 60, true).show();
            }
        });

        picktimeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(InstructorEditClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int t = FitClass.convertTime(hourOfDay, minute);
                        if(t > time)
                            timeEnd = t;
                        else {
                            AlertDialog.Builder nullParams = new AlertDialog.Builder(InstructorEditClassActivity.this);
                            nullParams.setCancelable(true);
                            nullParams.setTitle("*Error*");
                            nullParams.setMessage("End time must be after the start time");
                            AlertDialog error = nullParams.create();
                            error.show();
                            timeEnd = t + 60;
                        }
                    }
                }, timeEnd/60, timeEnd % 60, true).show();
            }
        });
    }

    // allows instructor to select the date
    private void handleDateButton(){
        Calendar calendar=Calendar.getInstance();
        int date= calendar.get(Calendar.DATE);
        int year= calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {

                Calendar calendar1= Calendar.getInstance();
                calendar1.set(Calendar.DATE,date);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.YEAR,year);
                int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);

                CharSequence dateCharSequence= DateFormat.format("MMM d, yyyy",calendar1);
                dateTextView.setText(dateCharSequence);

                if(dayOfWeek==1){
                    day=day.SUNDAY;
                }else if(dayOfWeek==2){
                    day=day.MONDAY;
                }else if(dayOfWeek==3){
                    day=day.TUESDAY;
                }else if(dayOfWeek==4){
                    day=day.WEDNESDAY;
                }else if(dayOfWeek==5){
                    day=day.THURSDAY;
                }else if(dayOfWeek==6){
                    day=day.FRIDAY;
                }else{
                    day=day.SATURDAY;
                }
            }
        },year, month, date);
        datePickerDialog.show();
    }

    public void checkButton(View v){
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
        //FirestoreDatabase.getInstance().getFitClassType(getIntent().getExtras().getString("classuid"), new OnTaskComplete<FitClassType>() {
        FirestoreDatabase.getInstance().getFitClass(getIntent().getExtras().getString("classuid"), new OnTaskComplete<FitClass>() {
            @Override
            public void onComplete(FitClass fcResult) {

                if(capText.getText() != null) {
                    capacity=Integer.parseInt(capText.getText().toString());
                } else {
                    capacity = 5;
                }

                fcResult.setCapacity(capacity);
                fcResult.setDifficulty(difficulty);
                fcResult.setTime(time);
                fcResult.setEndTime(timeEnd);
                fcResult.setDateObj(day);
                fcResult.setTeacherUID(AuthManager.getInstance().getCurrentUserData().getUid());

                fcResult.checkCollision(new OnTaskComplete<Boolean>() {
                    @Override
                    public void onComplete(Boolean result) {
                        if (result==true || capacity <= 0){
                            AlertDialog.Builder nullParams = new AlertDialog.Builder(InstructorEditClassActivity.this);
                            nullParams.setCancelable(true);
                            nullParams.setTitle("*Error*");
                            nullParams.setMessage("CLASSES CANNOT BE MADE ON THE SAME DAY AND CLASS CAPACITIES MUST BE GREATER THAN 0");
                            AlertDialog error = nullParams.create();
                            error.show();
                        }else{
                            FirestoreDatabase.getInstance().setFitClass(fcResult);
                            Intent i = new Intent(InstructorEditClassActivity.this, InstructorWelcomeActivity.class);
                            startActivity(i);
                        }
                    }
                });
            }
        });



    }

}