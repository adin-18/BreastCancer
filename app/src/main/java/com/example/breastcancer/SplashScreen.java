package com.example.breastcancer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth auth;
    private static final int SPLASH_DURATION = 2500; // Duration for splash screen in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();

        // Proceed to the next screen after the splash duration
        runNextScreen();
    }

    private void runNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to IntroActivity if user is not authenticated
                if (auth.getCurrentUser() == null) {
                    Intent nextIntent = new Intent(SplashScreen.this, IntroActivity.class);
                    startActivity(nextIntent);
                }
                finish(); // Close splash screen activity
            }
        }, SPLASH_DURATION);
    }

    private void setFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Use IMERSIVE_STICKY for better full-screen experience
        );
    }
}
