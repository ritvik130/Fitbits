package me.seg.fitbites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewClass extends AppCompatActivity {
    private String className, classDescription;
    private TextView classNameInput, classDescriptionInput;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_class);
        classNameInput=(TextView) findViewById(R.id.textView7);
        classDescriptionInput=(TextView) findViewById(R.id.textView8);
        submitButton = (Button) findViewById(R.id.button1);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                className = classNameInput.getText().toString();
                classDescription = classDescriptionInput.getText().toString();
                 newFitClass(className, classDescription);
            }
        });
    }
    private void newFitClass(String className, String classDescription ){
        createFitClassType(className, classDescription);
    }

}
