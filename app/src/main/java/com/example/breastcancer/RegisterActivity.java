package com.example.breastcancer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.breastcancer.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    EditText regName, regEmail,regPass,regConfirmPass;
    Button signUpButton;
    LinearLayout signInText;
    String imgUri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+(\\.[a-z]+)?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        regName = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regConfirmPass = findViewById(R.id.regConfirmPass);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setBackgroundResource(R.drawable.intro_signin);

        signInText = findViewById(R.id.signInText);

        signUpButton.setOnClickListener(view -> {
            String name = regName.getEditableText().toString();
            String email = regEmail.getEditableText().toString();
            String password = regPass.getEditableText().toString();
            String cPassword = regConfirmPass.getEditableText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cPassword)) {
                Toast.makeText(RegisterActivity.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();
            } else if (!email.matches(emailPattern)) {
                regEmail.setError("Invalid Email");
                Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
            } else if (password.length() <= 6) {
                regPass.setError("Password Too Short");
                Toast.makeText(RegisterActivity.this, "Please enter more than 6 characters", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(cPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference reference = database.getReference().child("users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                        StorageReference storageReference = storage.getReference().child("upload").child(auth.getCurrentUser().getUid());
                        imgUri = "https://firebasestorage.googleapis.com/v0/b/breastcancer-5c20a.appspot.com/o/Profile_image.jpg?alt=media&token=0dc5040f-1b59-4afe-b06b-9c752a540f44";
                        Users users = new Users(auth.getCurrentUser().getUid(), name, email, imgUri);

                        reference.setValue(users).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        // Log the error message from Firebase for more details
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
        signInText.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });


    }
}