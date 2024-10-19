package com.example.breastcancer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class ResultActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private TextView cancerRiskPredictionTextView;
    private Button selfExaminationButton;
    private Button doctorDetailsButton;
    private Button logoutButton;  // Add logout button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize Views
        userNameTextView = findViewById(R.id.user_name);
        cancerRiskPredictionTextView = findViewById(R.id.cancerRiskPrediction);
        selfExaminationButton = findViewById(R.id.self_examination_button);
        doctorDetailsButton = findViewById(R.id.doctor_details_button);
        logoutButton = findViewById(R.id.logout_button);  // Initialize logout button

        // Get Intent Data
        Intent intent = getIntent();
        String userName = intent.getStringExtra("USER_NAME");
        String cancerRiskPrediction = intent.getStringExtra("CANCER_RISK_PREDICTION");

        // Set User Name and Prediction
        userNameTextView.setText(userName);
        cancerRiskPredictionTextView.setText("Cancer Risk Prediction: " + cancerRiskPrediction);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set button click listeners
        selfExaminationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Self Examination Activity
                Intent selfExamIntent = new Intent(ResultActivity.this, SelfExaminationActivity.class);
                startActivity(selfExamIntent);
            }
        });

        doctorDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Doctor Details Activity
                Intent doctorDetailsIntent = new Intent(ResultActivity.this, DoctorDetailsActivity.class);
                startActivity(doctorDetailsIntent);
            }
        });

        // Logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout user
                FirebaseAuth.getInstance().signOut();
                // Redirect to Login Activity
                Intent loginIntent = new Intent(ResultActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear all activities
                startActivity(loginIntent);
                finish(); // Close current activity
            }
        });
    }
}

