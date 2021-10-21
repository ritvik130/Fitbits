package me.seg.fitbites.layouts.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.seg.fitbites.R;

public class AdminClassOptionsActivity extends AppCompatActivity {
    private Button back;
    private Button addClass;
    private Button removeClass;
    private Button editClass;
    private Button createTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_classOptions);
        back = (Button) findViewById(R.id.OptScreenBackBtn);
        addClass= (Button) findViewById(R.id.AddClassBtn);
        removeClass= (Button) findViewById(R.id.RemoveClassBtn);
        editClass= (Button) findViewById(R.id.EditClassBtn);
        createTag= (Button) findViewById(R.id.CreateTagBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewClass();
            }
        });
        editClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClass();
            }
        });
        removeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClass();
            }
        });
        createTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTag();
            }
        });
    }
    public void back(){
        // goes back to admin login class
        Intent intent= new Intent(this, AdminWelcomeActivity.class );
        startActivity(intent);
    }
    public void addNewClass(){
        // goes to add new class screen
        Intent intent= new Intent(this, AdminAddClassActivity.class );
        startActivity(intent);
    }
    public void editClass(){
        // goes to Change class screen
        Intent intent= new Intent(this, AdminEditClass_SearchActivity.class );
        startActivity(intent);
    }
    public void removeClass(){
        // goes to cancel class screen
        Intent intent= new Intent(this, AdminDeleteClassActivity.class);
        startActivity(intent);
    }
    public void createTag(){

    }
}
