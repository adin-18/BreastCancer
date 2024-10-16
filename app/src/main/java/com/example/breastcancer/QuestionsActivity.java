package com.example.breastcancer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.HashMap;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {

    private EditText ageInput;
    private Spinner genderSpinner, bloodTypeSpinner;
    private CheckBox familyHistoryCheckbox, smokerCheckbox, alcoholCheckbox, physicalActivityCheckbox;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private CircleImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Initialize Firebase Auth, Database, and Storage
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        // Initialize views
        ageInput = findViewById(R.id.age_input);
        genderSpinner = findViewById(R.id.gender_spinner);
        bloodTypeSpinner = findViewById(R.id.blood_type_spinner);
        familyHistoryCheckbox = findViewById(R.id.family_history_checkbox);
        smokerCheckbox = findViewById(R.id.smoker_checkbox);
        alcoholCheckbox = findViewById(R.id.alcohol_checkbox);
        physicalActivityCheckbox = findViewById(R.id.physical_activity_checkbox);
        submitButton = findViewById(R.id.submit_button);
        profileImageView = findViewById(R.id.profile_image);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Load user data
        loadUserData();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInputs();
            }
        });
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userName = currentUser.getDisplayName();
            String userEmail = currentUser.getEmail();
            String userPhotoUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null;

            if (userPhotoUrl != null && !userPhotoUrl.isEmpty()) {
                loadProfileImage(userPhotoUrl);
            } else {
                loadDefaultProfileImage();
            }
        }
    }

    private void loadProfileImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(profileImageView);
    }

    private void loadDefaultProfileImage() {
        // Assuming the default profile image is stored at "default_profile_image.jpg" in Firebase Storage
        StorageReference defaultImageRef = mStorage.child("Profile_image.jpg");

        defaultImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri)
                    .into(profileImageView);
        }).addOnFailureListener(e -> {
            // If failed to load the default image, you can set a local drawable as a fallback
            profileImageView.setImageResource(R.drawable.profile_image);
            Toast.makeText(QuestionsActivity.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveUserInputs() {
        String age = ageInput.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String bloodType = bloodTypeSpinner.getSelectedItem().toString();
        boolean familyHistory = familyHistoryCheckbox.isChecked();
        boolean isSmoker = smokerCheckbox.isChecked();
        boolean consumesAlcohol = alcoholCheckbox.isChecked();
        boolean isPhysicallyActive = physicalActivityCheckbox.isChecked();

        if (validateInputs(age, gender, bloodType)) {
            // Save to Firebase under the user's ID
            String userId = mAuth.getCurrentUser().getUid();
            Map<String, Object> userInputs = new HashMap<>();
            userInputs.put("age", age);
            userInputs.put("gender", gender);
            userInputs.put("bloodType", bloodType);
            userInputs.put("familyHistory", familyHistory);
            userInputs.put("isSmoker", isSmoker);
            userInputs.put("consumesAlcohol", consumesAlcohol);
            userInputs.put("isPhysicallyActive", isPhysicallyActive);

            mDatabase.child("users").child(userId).child("personalQuestions").setValue(userInputs)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(QuestionsActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(QuestionsActivity.this, ResultActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(QuestionsActivity.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private boolean validateInputs(String age, String gender, String bloodType) {
        if (age.isEmpty()) {
            ageInput.setError("Age is required");
            return false;
        }
        if (gender.equals("Select Gender")) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bloodType.equals("Select Blood Type")) {
            Toast.makeText(this, "Please select a blood type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
