package me.seg.fitbites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.seg.fitbites.firebase.AuthManager;

public class InstructorWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_welcome);

        TextView title = findViewById(R.id.Inst_Title);
        title.setText(title.getText().toString().replace("x", AuthManager.getInstance().getCurrentUserData().getFirstName()));

        Button signout = (Button) findViewById(R.id.inst_signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.getInstance().signoutUser();
                Intent i = new Intent(InstructorWelcome.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}