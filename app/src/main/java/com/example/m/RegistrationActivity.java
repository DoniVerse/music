package com.example.m;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m.data.AppDatabase;
import com.example.m.data.dao.UserDao;
import com.example.m.data.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationActivity extends AppCompatActivity {
    private TextInputLayout nameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private MaterialButton signupButton;
    private TextView loginPrompt;
    private ProgressBar progressBar;
    private UserDao userDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        // Initialize views
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        signupButton = findViewById(R.id.signupButton);
        loginPrompt = findViewById(R.id.loginPrompt);
        progressBar = findViewById(R.id.progressBar);

        // Initialize database
        userDao = AppDatabase.getInstance(this).userDao();
        executorService = Executors.newSingleThreadExecutor();

        // Set up click listeners
        signupButton.setOnClickListener(v -> attemptRegistration());
        loginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void attemptRegistration() {
        // Reset errors
        nameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        // Get values
        String name = ((TextInputEditText) nameLayout.getEditText()).getText().toString().trim();
        String email = ((TextInputEditText) emailLayout.getEditText()).getText().toString().trim();
        String password = ((TextInputEditText) passwordLayout.getEditText()).getText().toString().trim();
        String confirmPassword = ((TextInputEditText) confirmPasswordLayout.getEditText()).getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for empty fields
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordLayout.setError("Confirm password is required");
            focusView = confirmPasswordLayout;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            focusView = passwordLayout;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            focusView = emailLayout;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailLayout.setError("Invalid email address");
            focusView = emailLayout;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            nameLayout.setError("Name is required");
            focusView = nameLayout;
            cancel = true;
        }

        if (!password.equals(confirmPassword)) {
            passwordLayout.setError("Passwords do not match");
            confirmPasswordLayout.setError("Passwords do not match");
            focusView = passwordLayout;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show progress
            progressBar.setVisibility(View.VISIBLE);
            signupButton.setEnabled(false);

            // Attempt to register
            executorService.execute(() -> {
                User existingUser = userDao.findByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        signupButton.setEnabled(true);
                        emailLayout.setError("Email already registered");
                        emailLayout.getEditText().requestFocus();
                    });
                } else {
                    // Create new user
                    User newUser = new User(name, email, password);
                    long userId = userDao.insert(newUser);

                    if (userId != -1) {
                        // Registration successful
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            signupButton.setEnabled(true);
                            Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        // Registration failed
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            signupButton.setEnabled(true);
                            Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}