package me.seg.fitbites.layouts.instructor;
import static me.seg.fitbites.data.FitClass.createClass;
import static me.seg.fitbites.data.FitClass.searchClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import me.seg.fitbites.firebase.FirestoreDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import me.seg.fitbites.data.Days;
import me.seg.fitbites.data.Difficulty;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.admin.AdminDeleteClassActivity;
import me.seg.fitbites.layouts.admin.AdminWelcomeActivity;
import java.util.ArrayList;

public class InstructorAddClassActivity extends AppCompatActivity{

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addCLass,backButton, dateButton, searchButton;
    private TextView dateTextView, durationTextView,classCapacity;
    private EditText classText;
    private Difficulty difficulty;
    private String difficultySelection;
    private int capacity, duration;
    private Days day;
    private FitClass searchSelection;
    private ListView result;
    private ArrayList<FitClass> resultList= new ArrayList<>();
    FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_class);

        radioGroup= findViewById(R.id.radioGroup);
        dateButton= findViewById(R.id.addDateButton);
        durationTextView=findViewById(R.id.DurationTextView);
        dateTextView=findViewById(R.id.dateTextView);
        backButton=findViewById(R.id.AddClassBackBtn);
        searchButton=findViewById(R.id.searchButton1);
        classText=findViewById(R.id.classText);
        addCLass=findViewById(R.id.FinalizeAddClassBtn);
        classCapacity=findViewById(R.id.capacityTextView);
        result=findViewById(R.id.listView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClass(classText.getText().toString(), new OnTaskComplete<FitClass[]>() {
                    @Override
                    public void onComplete(FitClass[] result) {
                        handleSearchButton(result);
                    }
                });
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(InstructorAddClassActivity.this,InstructorWelcomeActivity.class);
                startActivity(i);
            }
        });

        addCLass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddClassButton();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                CharSequence dateCharSequence= DateFormat.format("MMM d, yyyy",calendar1);
                dateTextView.setText(dateCharSequence);
            }
        },year, month, date);
        datePickerDialog.show();

        if(dayOfWeek==1){
            day=day.SUNDAY;
        }else if(dayOfWeek==2){
            day=day.MONDAY;
        }else if(dayOfWeek==3){
            day=day.TUESDAY;
        }else if(dayOfWeek==4){
            day=day.WEDENSDAY;
        }else if(dayOfWeek==5){
            day=day.THURSDAY;
        }else if(dayOfWeek==6){
            day=day.FRIDAY;
        }else{
            day=day.SATURDAY;
        }
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
        FirestoreDatabase.getInstance().getFitClassType(getIntent().getExtras().getString("classuid"), new OnTaskComplete<FitClassType>() {
            @Override
            public void onComplete(FitClassType result) {
                capacity=Integer.parseInt(classCapacity.getText().toString());
                duration=Integer.parseInt(durationTextView.getText().toString());

                FitClass fitClass= FitClass.createClass(result);
                fitClass.setCapacity(capacity);
                fitClass.setDifficulty(difficulty);
                fitClass.setTime(duration);

                searchSelection.checkCollision(new OnTaskComplete<Boolean>() {
                    @Override
                    public void onComplete(Boolean result) {
                        if (result==true){
                            AlertDialog.Builder nullParams = new AlertDialog.Builder(InstructorAddClassActivity.this);
                            nullParams.setCancelable(true);
                            nullParams.setTitle("*Error*");
                            nullParams.setMessage("All information must be filled!");
                            AlertDialog error = nullParams.create();
                            error.show();
                        }else{
                            FirestoreDatabase.getInstance().setFitClass(fitClass);
                        }
                    }
                });
            }
        });



    }

}