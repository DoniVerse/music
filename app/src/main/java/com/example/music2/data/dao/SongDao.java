package com.example.music2.data.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.music2.database.Song;

import java.util.List;

@Dao
public interface SongDao {
    @Insert
    void insert(Song song);

    @Update
    void update(Song song);

    @Delete
    void delete(Song song);

    @Query("SELECT * FROM songs")
    LiveData<List<Song>> getAllSongs();

    @Query("SELECT * FROM songs WHERE id = :id")
    Song getSongById(int id);

    @Query("UPDATE songs SET lastPlayed = :lastPlayed, playCount = playCount + 1 WHERE id = :songId")
    void updatePlayStats(int songId, long lastPlayed);
    @Query("SELECT * FROM songs WHERE lastPlayedTimestamp > 0 ORDER BY lastPlayedTimestamp DESC LIMIT 10")
    List<Song> getRecentlyPlayedSongs();
    @Query("SELECT * FROM songs WHERE artistId = :artistId")
    LiveData<List<Song>> getSongsByArtist(int artistId);
    @Query("SELECT * FROM songs ORDER BY playCount DESC LIMIT 10")
    List<Song> getMostPlayedSongs();


}