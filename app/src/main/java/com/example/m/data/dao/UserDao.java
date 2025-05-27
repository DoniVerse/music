package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE username = :username")
    User findByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email")
    User findByEmail(String email);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();
}
