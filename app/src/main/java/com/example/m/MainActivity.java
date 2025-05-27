package com.example.m;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.m.data.AppDatabase;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnLogin;
    private MaterialButton btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        AppDatabase.getInstance(this);

        // Initialize buttons
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);

        // Set click listeners
        btnLogin.setOnClickListener(v -> navigateToLogin());
        btnSignup.setOnClickListener(v -> navigateToSignup());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close this activity so user can't go back
    }

    private void navigateToSignup() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish(); // Close this activity so user can't go back
    }
}
