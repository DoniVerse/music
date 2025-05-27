package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.Playlist;
import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert
    long insert(Playlist playlist);

    @Update
    void update(Playlist playlist);

    @Delete
    void delete(Playlist playlist);

    @Query("SELECT * FROM playlists WHERE userId = :userId")
    List<Playlist> findByUserId(int userId);

    @Query("SELECT * FROM playlists")
    List<Playlist> getAllPlaylists();
}
