package com.example.music2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.music2.database.PlaylistSongJoin;
import com.example.music2.database.Song;

import java.util.List;

@Dao
public interface PlaylistSongJoinDao {

    @Query("DELETE FROM playlist_song_join WHERE playlistId = :playlistId")
    void clearPlaylist(int playlistId);

    @Insert
    void insert(PlaylistSongJoin join);

    @Transaction
    default void replaceSongsInPlaylist(int playlistId, List<Song> songs) {
        clearPlaylist(playlistId);
        for (Song song : songs) {
            insert(new PlaylistSongJoin(playlistId, song.getId()));
        }
    }
}
