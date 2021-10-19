package me.seg.fitbites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChangeClassScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button backBTN,changeClassBTN;
        TextView className,classDescription;
        setContentView(R.layout.activity_change_class_screen);

        backBTN = findViewById(R.id.button);
        className = findViewById(R.id.editTextTextPersonName9);
        classDescription = findViewById(R.id.editTextTextPersonName10);
        changeClassBTN = findViewById(R.id.button2);

        final ChangeClassScreen current = this;

        backBTN.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(current, AdminLogin.class);
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
                Intent intent = new Intent(current, Search_Class_Edit.class);

                startActivity(intent);
            }

        });



    }
}