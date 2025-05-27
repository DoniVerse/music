package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.PlaylistSong;
import java.util.List;

@Dao
public interface PlaylistSongDao {
    @Insert
    long insert(PlaylistSong playlistSong);

    @Delete
    void delete(PlaylistSong playlistSong);

    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId")
    List<PlaylistSong> findByPlaylistId(int playlistId);

    @Query("SELECT * FROM playlist_songs WHERE songId = :songId")
    List<PlaylistSong> findBySongId(int songId);
}
