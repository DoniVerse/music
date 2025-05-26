package com.example.music2.database;



import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
@Entity(tableName = "users")
public class User {
        @PrimaryKey(autoGenerate = true)
        private long userId;

        @ColumnInfo(name = "username")
        private String username;

        @ColumnInfo(name = "password")
        private String password;

        @ColumnInfo(name = "email")
        private String email;

        @ColumnInfo(name = "created_at")
        private long createdAt;

        public User(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.createdAt = System.currentTimeMillis();
        }

        // Getters
        public long getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getEmail() { return email; }
        public long getCreatedAt() { return createdAt; }

        // Setters
        public void setUserId(long userId) { this.userId = userId; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        public void setEmail(String email) { this.email = email; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    }

