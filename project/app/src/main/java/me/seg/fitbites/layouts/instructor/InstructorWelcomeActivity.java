package me.seg.fitbites.layouts.instructor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.seg.fitbites.MainActivity;
import me.seg.fitbites.R;
import me.seg.fitbites.firebase.AuthManager;

public class InstructorWelcomeActivity extends AppCompatActivity {
    private TextView title;
    private Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_welcome);

        title = findViewById(R.id.Inst_Title);
        title.setText(title.getText().toString().replace("x", AuthManager.getInstance().getCurrentUserData().getFirstName()));

        signout = (Button) findViewById(R.id.inst_signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.getInstance().signoutUser();
                Intent i = new Intent(InstructorWelcomeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}