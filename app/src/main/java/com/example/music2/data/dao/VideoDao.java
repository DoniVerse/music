package com.example.music2.data.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.music2.database.Video;

import java.util.List;

@Dao
public interface VideoDao {

    @Insert
    long insert(Video video);

    @Update
    void update(Video video);

    @Delete
    void delete(Video video);

    @Query("SELECT * FROM videos WHERE userId = :userId")
    List<Video> getVideosByUserId(long userId);

    @Query("SELECT * FROM videos WHERE id = :videoId LIMIT 1")
    Video getVideoById(long videoId);
}
