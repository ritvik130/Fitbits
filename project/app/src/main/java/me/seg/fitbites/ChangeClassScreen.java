package me.seg.fitbites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChangeClassScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_classScreen);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Change Class");
    }
}