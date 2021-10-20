package me.seg.fitbites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.seg.fitbites.firebase.AuthManager;

public class AdminLogin extends AppCompatActivity {
    private Button manageClasses;
    private Button deleteAccounts;
    private Button newClass;
    private Button signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        // adds the ids of the buttons as on the XML file
        manageClasses = (Button) findViewById(R.id.ManageClasses);
        deleteAccounts = (Button) findViewById(R.id.DeleteAccounts);
        newClass=(Button) findViewById(R.id.NewClass);
        signOut = (Button) findViewById(R.id.admin_Signout);

        manageClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageClasses();
            }
        });

        deleteAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteAccounts();
            }
        });
        newClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewClass();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.getInstance().signoutUser();
                Intent i = new Intent(AdminLogin.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
    public void openManageClasses(){
        //opens page option screen
        //change name to manage screen
        Intent intent= new Intent(this,ManageClassScreen.class );
        startActivity(intent);
    }

    public void openDeleteAccounts(){
        // opens page search_user
        Intent intent= new Intent(this,SearchUser.class );
        startActivity(intent);
    }


    public void openNewClass(){
        // opens add new class
        Intent intent= new Intent(this,AddNewClass.class );
        startActivity(intent);
    }
}
