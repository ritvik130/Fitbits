package me.seg.fitbites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import me.seg.fitbites.firebase.AuthManager;

public class GymMemberWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member_welcome);

        TextView title = findViewById(R.id.Mem_Title);
        title.setText(title.getText().toString().replace("x", AuthManager.getInstance().getCurrentUserData().getFirstName()));
    }
}