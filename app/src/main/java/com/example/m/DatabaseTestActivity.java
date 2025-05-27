package com.example.m;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.m.data.AppDatabase;
import com.example.m.data.dao.UserDao;
import com.example.m.data.entities.User;

public class DatabaseTestActivity extends AppCompatActivity {
    private static final String TAG = "DatabaseTest";
    private AppDatabase database;
    private UserDao userDao;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        // Initialize database
        database = AppDatabase.getInstance(this);
        userDao = database.userDao();
        mainHandler = new Handler(Looper.getMainLooper());

        // Run database operations in a background thread
        new Thread(() -> {
            // Test creating a user
            User user = new User("testUser", "test@example.com", "password123");
            long userId = userDao.insert(user);
            Log.d(TAG, "Created user with ID: " + userId);

            // Test retrieving the user
            User retrievedUser = userDao.findByUsername("testUser");
            if (retrievedUser != null) {
                Log.d(TAG, "Retrieved user: " + retrievedUser.getUsername());
            } else {
                Log.d(TAG, "Failed to retrieve user");
            }

            // Update UI on main thread if needed
            mainHandler.post(() -> {
                // Update UI here if needed
            });
        }).start();
    }
}
