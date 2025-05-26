package com.example.music2.data.dao;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.music2.database.PlaylistSong;
import com.example.music2.database.Song;

import java.util.List;

@Dao
public interface PlaylistSongDao {

    @Insert
    long insert(PlaylistSong playlistSong);

    @Delete
    void delete(PlaylistSong playlistSong);

    // Get all songs in a specific playlist
    @Query("SELECT songs.* FROM songs " +
            "INNER JOIN playlist_songs ON songs.id = playlist_songs.songId " +
            "WHERE playlist_songs.playlistId = :playlistId")
    LiveData<List<Song>> getSongsInPlaylist(int playlistId);

    // Optional: delete all songs from a specific playlist
    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    void deleteAllFromPlaylist(int playlistId);
    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId LIMIT 1")
    PlaylistSong getSongInPlaylist(int playlistId, long songId);

    // Optional: check if a song already exists in the playlist
    @Query("SELECT COUNT(*) FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    int isSongInPlaylist(int playlistId, int songId);
}
