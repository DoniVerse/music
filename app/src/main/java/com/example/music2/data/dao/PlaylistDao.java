package com.example.music2.data.dao;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.music2.database.Playlist;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert
    long insert(Playlist playlist);

    @Update
    void update(Playlist playlist);

    @Delete
    void delete(Playlist playlist);

    // Get all playlists for a specific user that are not system playlists
    @Query("SELECT * FROM playlists WHERE userId = :userId AND isSystemPlaylist = 0")
    LiveData<List<Playlist>> getUserPlaylists(long userId);

    @Query("SELECT * FROM playlists WHERE userId = :userId AND isSystemPlaylist = 1")
    List<Playlist> getSystemPlaylistsSync(long userId);
    @Query("SELECT * FROM playlists WHERE userId = :userId")
    List<Playlist> getPlaylistsByUserNow(long userId);
    // Get all system playlists for a user
    @Query("SELECT * FROM playlists WHERE userId = :userId AND isSystemPlaylist = 1")
    LiveData<List<Playlist>> getSystemPlaylistsByUser(long userId);

    @Query("SELECT * FROM playlists WHERE userId = :userId AND name = :name LIMIT 1")
    Playlist getSystemPlaylistByName(long userId, String name);
}

