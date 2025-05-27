package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.Song;
import java.util.List;

@Dao
public interface SongDao {
    @Insert
    long insert(Song song);

    @Update
    void update(Song song);

    @Delete
    void delete(Song song);

    @Query("SELECT * FROM songs WHERE artistId = :artistId")
    List<Song> findByArtistId(int artistId);

    @Query("SELECT * FROM songs WHERE albumId = :albumId")
    List<Song> findByAlbumId(int albumId);

    @Query("SELECT * FROM songs")
    List<Song> getAllSongs();

    @Query("UPDATE songs SET playCount = playCount + 1, lastPlayed = :currentTime WHERE id = :songId")
    void incrementPlayCount(int songId, String currentTime);
}
