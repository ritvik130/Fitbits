package me.seg.fitbites.layouts.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.seg.fitbites.R;
import me.seg.fitbites.data.FitClassType;

public class AdminEditClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button backBTN,changeClassBTN;
        TextView className,classDescription;
        setContentView(R.layout.activity_admin_editClass);

        backBTN = findViewById(R.id.bkbutton);
        className = findViewById(R.id.editTextTextPersonName9);
        classDescription = findViewById(R.id.editTextTextPersonName10);
        changeClassBTN = findViewById(R.id.button2);

        final AdminEditClassActivity current = this;

        backBTN.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(current, AdminWelcomeActivity.class);
                startActivity(intent);
            }

        });

        changeClassBTN.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                String uid= getIntent().getExtras().getString("uid");
                FitClassType c= new FitClassType(uid, className.getText().toString(), classDescription.getText().toString());
                c.updateDatabase();
                Intent intent = new Intent(current, AdminEditClass_SearchActivity.class);

                startActivity(intent);
            }

        });



    }
}