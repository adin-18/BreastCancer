package com.example.breastcancer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PersonalData extends AppCompatActivity {

    private EditText inputAge, inputFamilyHistory, inputBMI;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

            inputAge = findViewById(R.id.input_age1);
            inputFamilyHistory = findViewById(R.id.input_family_history1);
            inputBMI = findViewById(R.id.input_bmi1);
            buttonSubmit = findViewById(R.id.button_submit1);

            Button myButton = findViewById(R.id.button_submit1);

            // Set an OnClickListener to the button
            myButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Create an Intent to start the new activity
                                                Intent intent = new Intent(PersonalData.this,QuesActivity.class);
                                                startActivity(intent);
                                            }
                                        };

            Toast.makeText(PersonalData.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();

        };
    }
}