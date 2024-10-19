package com.example.breastcancer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView userNameTextView;
    private CircleImageView profileImage;
    private ImageButton selfExamButton, predictRiskButton, doctorDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        userNameTextView = findViewById(R.id.userNameTextView);
        profileImage = findViewById(R.id.profile_image);
        selfExamButton = findViewById(R.id.selfExamButton);
        predictRiskButton = findViewById(R.id.predictRiskButton);
        doctorDetailsButton = findViewById(R.id.doctorDetailsButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userNameTextView.setText(currentUser.getDisplayName());
            loadProfileImage();
        }

        profileImage.setOnClickListener(v -> showLogoutMenu());

        selfExamButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SelfExaminationActivity.class)));
        predictRiskButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, QuestionsActivity.class)));
        doctorDetailsButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, DoctorDetailsActivity.class)));
    }

    private void loadProfileImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileImageRef = storageRef.child("Profile_image.jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.profile_image)
                    .into(profileImage);
        }).addOnFailureListener(e -> {
            // Load default image if failed to load from Firebase
            profileImage.setImageResource(R.drawable.profile_image);
        });
    }

    private void showLogoutMenu() {
        PopupMenu popup = new PopupMenu(HomeActivity.this, profileImage);
        popup.getMenuInflater().inflate(R.menu.drawer_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                logout();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void logout() {
        mAuth.signOut();
        clearSharedPreferences();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    private void clearSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
    }
}