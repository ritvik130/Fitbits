package me.seg.fitbites.layouts.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.seg.fitbites.R;
import me.seg.fitbites.data.FitClassType;

public class AdminAddClassActivity extends AppCompatActivity {
    private String className, classDescription;
    private TextView classNameInput, classDescriptionInput;
    private Button submitButton, bkBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addClass);
        classNameInput=(TextView) findViewById(R.id.textView7);
        classDescriptionInput=(TextView) findViewById(R.id.textView8);
        submitButton = (Button) findViewById(R.id.button1);
        bkBtn = (Button) findViewById(R.id.add_btn_bk);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                className = classNameInput.getText().toString();
                classDescription = classDescriptionInput.getText().toString();
                newFitClass(className, classDescription);
                Intent i = new Intent(AdminAddClassActivity.this, AdminClassOptionsActivity.class);
                startActivity(i);
            }
        });

        bkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminAddClassActivity.this, AdminWelcomeActivity.class);
                startActivity(i);
            }
        });
    }
    private void newFitClass(String className, String classDescription ){
        FitClassType.createFitClassType(className, classDescription);
    }

}
