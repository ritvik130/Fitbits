package me.seg.fitbites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class SearchClass extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_class_screen);


        Button searchButton;
        TextView classtext;

        searchButton = findViewById(R.id.searchButton);
        classtext = findViewById(R.id.classtext);

        //final ChangeClassScreen current = this;


        searchButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                FitClassType.searchClass(classtext.getText().toString(), new OnTaskComplete<FitClassType[]>() {
                    @Override
                    public void onComplete(FitClassType[] result) {
                        placeIntoResults(result);
                    }
                });

            }

        });


    }

    public void placeIntoResults(FitClassType[] r){
        LinearLayout layout = (LinearLayout)findViewById(R.id.searchresultslayout);
        LinearLayout.LayoutParams layoutP= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (FitClassType c: r){
            Button button= new Button(this);
            button.setText(c.getClassName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (DialogInterface.BUTTON_POSITIVE == which) {
                                FirestoreDatabase.getInstance().deleteFitClassType(c);
                                Intent intent = new Intent(SearchClass.this, AdminLogin.class);
                                startActivity(intent);
                            }

                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchClass.this);
                    builder.setMessage("Are you sure you want to remove this class?")
                            .setPositiveButton("Yes",dialogListener)
                                .setNegativeButton("No", dialogListener);


                }
            });
            layout.addView(button, layoutP);

        }

    }

}
