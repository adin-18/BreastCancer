package com.example.breastcancer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionsActivity extends AppCompatActivity {

    private EditText ageInput, bmiInput;
    private Spinner physicalActivitySpinner, dietSpinner;
    private CheckBox familyHistoryCheckbox, smokerCheckbox, alcoholCheckbox;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private CircleImageView profileImageView;
    private Interpreter tflite;
    private FirebaseUser currentUser;
    private TextView resultTextView;
    private View resultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Initialize Firebase Auth, Database, and Storage
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        // Check if user is logged in
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not logged in, redirect to login page
            Intent intent = new Intent(QuestionsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Prevent returning to this activity
            return; // Exit onCreate
        }

        // Initialize views
        ageInput = findViewById(R.id.age_input);
        bmiInput = findViewById(R.id.bmi_input);
        physicalActivitySpinner = findViewById(R.id.physical_activity_spinner);
        dietSpinner = findViewById(R.id.diet_spinner);
        familyHistoryCheckbox = findViewById(R.id.family_history_checkbox);
        smokerCheckbox = findViewById(R.id.smoker_checkbox);
        alcoholCheckbox = findViewById(R.id.alcohol_checkbox);
        submitButton = findViewById(R.id.submit_button);
        profileImageView = findViewById(R.id.profile_image);
        resultTextView = findViewById(R.id.result_text_view);
        resultLayout = findViewById(R.id.result_layout);

        // Set up spinner for Physical Activity and Diet
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this,
                R.array.physical_activity_array, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        physicalActivitySpinner.setAdapter(activityAdapter);

        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(this,
                R.array.diet_array, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(dietAdapter);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Load user data
        loadUserData();

        // Load the TensorFlow Lite model
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading model", Toast.LENGTH_SHORT).show();
            finish(); // Exit if model loading fails
        }

        // Set onClickListener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInputsAndPredict();
            }
        });
    }

    private void loadUserData() {
        if (currentUser != null) {
            String userPhotoUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null;
            if (userPhotoUrl != null && !userPhotoUrl.isEmpty()) {
                loadProfileImage(userPhotoUrl);
            } else {
                loadDefaultProfileImage();
            }
        }
    }

    private void loadProfileImage(String imageUrl) {
        Glide.with(this).load(imageUrl).into(profileImageView);
    }

    private void loadDefaultProfileImage() {
        StorageReference defaultImageRef = mStorage.child("Profile_image.jpg");
        defaultImageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(this).load(uri).into(profileImageView))
                .addOnFailureListener(e -> {
                    profileImageView.setImageResource(R.drawable.profile_image);
                    Toast.makeText(QuestionsActivity.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserInputsAndPredict() {
        String age = ageInput.getText().toString().trim();
        String bmi = bmiInput.getText().toString().trim();
        String physicalActivity = physicalActivitySpinner.getSelectedItem().toString();
        String diet = dietSpinner.getSelectedItem().toString();
        boolean familyHistory = familyHistoryCheckbox.isChecked();
        boolean isSmoker = smokerCheckbox.isChecked();
        boolean consumesAlcohol = alcoholCheckbox.isChecked();

        if (validateInputs(age, bmi)) {
            float[] inputFeatures = new float[]{
                    Float.parseFloat(age),
                    Float.parseFloat(bmi),
                    convertPhysicalActivityToFloat(physicalActivity),
                    convertDietToFloat(diet),
                    familyHistory ? 1.0f : 0.0f,
                    isSmoker ? 1.0f : 0.0f,
                    consumesAlcohol ? 1.0f : 0.0f
            };

            float riskScore = predictCancerRisk(inputFeatures);
            displayResult(riskScore);

            // Save user inputs to Firebase
            saveInputsToFirebase(age, bmi, physicalActivity, diet, familyHistory, isSmoker, consumesAlcohol);
        }
    }

    private void saveInputsToFirebase(String age, String bmi, String physicalActivity, String diet, boolean familyHistory, boolean isSmoker, boolean consumesAlcohol) {
        if (currentUser == null) return; // Ensure user is logged in

        // Create a Map to hold the input data
        Map<String, Object> userInputData = new HashMap<>();
        userInputData.put("age", age);
        userInputData.put("bmi", bmi);
        userInputData.put("physicalActivity", physicalActivity);
        userInputData.put("diet", diet);
        userInputData.put("familyHistory", familyHistory);
        userInputData.put("isSmoker", isSmoker);
        userInputData.put("consumesAlcohol", consumesAlcohol);

        // Save data under the logged-in user's unique ID
        String userId = currentUser.getUid();
        mDatabase.child("users").child(userId).child("inputs").setValue(userInputData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(QuestionsActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QuestionsActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs(String age, String bmi) {
        if (age.isEmpty()) {
            ageInput.setError("Age is required");
            return false;
        }
        if (bmi.isEmpty()) {
            bmiInput.setError("BMI is required");
            return false;
        }
        return true;
    }

    private float predictCancerRisk(float[] inputFeatures) {
        if (tflite == null) {
            Toast.makeText(this, "Model not loaded", Toast.LENGTH_SHORT).show();
            return -1;
        }

        float[][] outputScore = new float[1][1];
        tflite.run(inputFeatures, outputScore);
        return outputScore[0][0];
    }

    private void displayResult(float riskScore) {
        String resultText;
        if (riskScore < 0.3) {
            resultText = "Low Risk";
        } else if (riskScore < 0.7) {
            resultText = "Moderate Risk";
        } else {
            resultText = "High Risk";
        }

        resultTextView.setText("Cancer Risk: " + resultText + "\nScore: " + String.format("%.2f", riskScore));
        resultLayout.setVisibility(View.VISIBLE);
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("logistic_cancer_risk_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private float convertPhysicalActivityToFloat(String physicalActivity) {
        switch (physicalActivity) {
            case "Low":
                return 0.0f;
            case "Moderate":
                return 0.5f;
            case "High":
                return 1.0f;
            default:
                return 0.0f;
        }
    }

    private float convertDietToFloat(String diet) {
        switch (diet) {
            case "Poor":
                return 0.0f;
            case "Average":
                return 0.5f;
            case "Healthy":
                return 1.0f;
            default:
                return 0.0f;
        }
    }
}
