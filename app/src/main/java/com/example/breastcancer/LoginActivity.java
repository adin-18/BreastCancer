package com.example.breastcancer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailEditText, passwordEditText;
    Button signInButton;
    LinearLayout signUpText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+(\\.[a-z]+)?";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.loginPassword);
        signInButton = findViewById(R.id.signInButton);
        signInButton.setBackgroundResource(R.drawable.intro_signin);
        signUpText = findViewById(R.id.signUpText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        progressDialog.show();

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            dismissProgressDialog("Enter Valid Data");
        } else if (!email.matches(emailPattern)) {
            emailEditText.setError("Invalid Email");
            dismissProgressDialog("Invalid Email");
        } else if (password.length() <= 6) {
            passwordEditText.setError("Invalid Password");
            dismissProgressDialog("Please Enter more than 6 characters");
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        dismissProgressDialog(); // Call without message
                        startActivity(new Intent(LoginActivity.this, QuestionsActivity.class));
                    } else {
                        dismissProgressDialog("Error in Login. Try Again");
                        // Optionally log the error for debugging
                    }
                }
            });
        }
    }


    private void dismissProgressDialog(String message) {
        progressDialog.dismiss();
        if (message != null) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    // Overloaded method for no message
    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
