package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.Video;
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
    List<Video> findByUserId(int userId);

    @Query("SELECT * FROM videos")
    List<Video> getAllVideos();
}
