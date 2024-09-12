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

        auth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        emailEditText= findViewById(R.id.loginEmail);
        passwordEditText= findViewById(R.id.loginPassword);
        signInButton= findViewById(R.id.signInButton);
        signInButton.setBackgroundResource(R.drawable.intro_signin);
        signUpText=findViewById(R.id.signUpText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String email = emailEditText.getEditableText().toString();
                String password = passwordEditText.getEditableText().toString();

                if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    emailEditText.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (password.length()<=6) {
                    progressDialog.dismiss();
                    passwordEditText.setError("Invalid Password");
                    Toast.makeText(LoginActivity.this, "Please Enter more than 6 characters", Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, QuesActivity.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error in Login. Try Again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });



    }
}