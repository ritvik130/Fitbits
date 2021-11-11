package me.seg.fitbites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class AddClassScreen extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button addCLass,backButton, dateButton,timeButton, searchButton;
    TextView dateTextView, timeTextView,classCapacity,description;
    EditText classText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class_screen);

        radioGroup= findViewById(R.id.radioGroup);
        dateButton= findViewById(R.id.addDateButton);
        timeButton= findViewById(R.id.addTimeButton);
        dateTextView=findViewById(R.id.dateTextView);
        backButton=findViewById(R.id.AddClassBackBtn);
        searchButton=findViewById(R.id.searchButton1);
        classText=findViewById(R.id.classtext);

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
        final AddClassScreen current = this;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(current, InstructorWelcome.class);
                startActivity(intent);
            }
        });
    }

    public void placeIntoResults(FitClassType[] r){
        LinearLayout layout = (LinearLayout)findViewById(R.id.searchresultslayout);
        LinearLayout.LayoutParams layoutP= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (FitClassType c: r){
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
                                Intent intent = new Intent(AddClassScreen.this, AdminLogin.class);
                                startActivity(intent);
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddClassScreen.this);
                    builder.setMessage("Are you sure you want to remove this class?")
                            .setPositiveButton("Yes",dialogListener)
                            .setNegativeButton("No", dialogListener);
                    // builder.show(); needs to be added
                }
            });
            layout.addView(button, layoutP);
        }
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

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);
        Toast.makeText(this, radioButton.getText()+" selected", Toast.LENGTH_SHORT).show();
    }
}