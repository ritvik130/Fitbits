package me.seg.fitbites.layouts.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.seg.fitbites.R;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class AdminDeleteUserActivity extends AppCompatActivity {

    private String memberSearch;
    private Button searchButton, bkBtn;
    private EditText memberSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deleteUser);

        memberSearchBar= (EditText) findViewById(R.id.Member_SearchBar);
        searchButton= (Button) findViewById(R.id.SearchButton);
        bkBtn = (Button) findViewById(R.id.back_button_searchscreen);

        FirestoreDatabase.getInstance().getUserList(new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                placeIntoResults(result);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberSearch = memberSearchBar.getText().toString();
                UserData.searchUser(memberSearch, new OnTaskComplete<UserData[]>() {
                    @Override
                    public void onComplete(UserData[] result) {
                        placeIntoResults(result);
                    }
                });
            }
        });

        bkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminDeleteUserActivity.this, AdminWelcomeActivity.class);
                startActivity(i);
            }
        });
    }
    public void placeIntoResults(UserData[] r){
        LinearLayout layout = (LinearLayout)findViewById(R.id.susll);
        LinearLayout.LayoutParams layoutP= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layout.removeAllViews();
        boolean addedAtLeastOne = false;

        for (UserData c: r){
            addedAtLeastOne = true;

            Button button= new Button(this);
            button.setText(c.getEmail());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (DialogInterface.BUTTON_POSITIVE == which) {
                                AuthManager.getInstance().deleteAccount(c);
                                Intent intent = new Intent(AdminDeleteUserActivity.this, AdminWelcomeActivity.class);
                                startActivity(intent);
                            }

                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminDeleteUserActivity.this);
                    builder.setMessage("Are you sure you want to remove this User?")
                            .setPositiveButton("Yes",dialogListener)
                            .setNegativeButton("No", dialogListener);
                    builder.show();


                }
            });
            layout.addView(button, layoutP);

        }

        if(!addedAtLeastOne) {
            TextView v = new TextView(this);
            v.setText("No Users Found");
            layout.addView(v, layoutP);
        }

    }
}
