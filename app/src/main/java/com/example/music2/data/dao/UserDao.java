package com.example.music2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.music2.database.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    boolean usernameExists(String username);
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    boolean emailExists(String email);

}