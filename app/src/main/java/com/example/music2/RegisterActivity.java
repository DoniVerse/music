package com.example.music2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail;
    private Button btnRegister;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnRegister);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "music_db")
                .allowMainThreadQueries()
                .build();

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.userDao().emailExists(email)) {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(username, password, email);
            long userId = db.userDao().insert(newUser);

            new SessionManager(this).saveUserId(userId);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
