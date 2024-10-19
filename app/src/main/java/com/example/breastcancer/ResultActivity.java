package com.example.breastcancer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ResultActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private TextView cancerRiskPredictionTextView;
    private Button selfExaminationButton;
    private Button doctorDetailsButton;
    private Button logoutButton; // Add logout button
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize Views
        userNameTextView = findViewById(R.id.user_name);
        cancerRiskPredictionTextView = findViewById(R.id.cancerRiskPrediction);
        selfExaminationButton = findViewById(R.id.self_examination_button);
        doctorDetailsButton = findViewById(R.id.doctor_details_button);
        logoutButton = findViewById(R.id.logout_button); // Initialize logout button

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get Intent Data
        Intent intent = getIntent();
        String userName = intent.getStringExtra("USER_NAME");
        float cancerRiskPrediction = intent.getFloatExtra("CANCER_RISK_PREDICTION", 0); // Default value to avoid NullPointerException

        // Set User Name and Prediction
        userNameTextView.setText(userName);
        cancerRiskPredictionTextView.setText("Cancer Risk Prediction: " + cancerRiskPrediction);

        // Set onClickListener for Self Examination Button
        selfExaminationButton.setOnClickListener(v -> {
            Intent selfExaminationIntent = new Intent(ResultActivity.this, SelfExaminationActivity.class);
            startActivity(selfExaminationIntent);
        });

        // Set onClickListener for Doctor Details Button
        doctorDetailsButton.setOnClickListener(v -> {
            Intent doctorDetailsIntent = new Intent(ResultActivity.this, DoctorDetailsActivity.class);
            startActivity(doctorDetailsIntent);
        });

        // Set onClickListener for Logout Button
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut(); // Sign out the user
            Intent loginIntent = new Intent(ResultActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Close ResultActivity
        });

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
