package me.seg.fitbites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.seg.fitbites.firebase.AuthManager;

public class GymMemberWelcome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member_welcome);

        TextView title = findViewById(R.id.Mem_Title);
        title.setText(title.getText().toString().replace("x", AuthManager.getInstance().getCurrentUserData().getFirstName()));

        Button signout = (Button) findViewById(R.id.welGm_signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.getInstance().signoutUser();
                Intent i = new Intent(GymMemberWelcome.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}