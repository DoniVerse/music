package com.example.m;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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

public class LoginActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private MaterialButton loginButton;
    private TextView signupPrompt;
    private ProgressBar progressBar;
    private CheckBox rememberMeCheckbox;
    private UserDao userDao;
    private ExecutorService executorService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        loginButton = findViewById(R.id.loginButton);
        signupPrompt = findViewById(R.id.signupPrompt);
        progressBar = findViewById(R.id.progressBar);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        // Initialize database and shared preferences
        userDao = AppDatabase.getInstance(this).userDao();
        executorService = Executors.newSingleThreadExecutor();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            navigateToHome();
            return;
        }

        // Set up click listeners
        loginButton.setOnClickListener(v -> attemptLogin());
        signupPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        // Reset errors
        emailLayout.setError(null);
        passwordLayout.setError(null);

        // Get values
        String email = ((TextInputEditText) emailLayout.getEditText()).getText().toString().trim();
        String password = ((TextInputEditText) passwordLayout.getEditText()).getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for empty fields
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

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show progress
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);

            // Attempt to login
            executorService.execute(() -> {
                User user = userDao.findByEmail(email);
                if (user != null && user.getPassword().equals(password)) {
                    // Login successful
                    saveUserSession(user, rememberMeCheckbox.isChecked());
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        navigateToHome();
                    });
                } else {
                    // Login failed
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        passwordLayout.setError("Invalid password");
                        passwordLayout.getEditText().requestFocus();
                    });
                }
            });
        }
    }

    private void saveUserSession(User user, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getInt(KEY_USER_ID, -1) != -1;
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
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
