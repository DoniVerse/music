package com.example.music2.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "playlist_song_join",
        primaryKeys = {"playlistId", "songId"},
        foreignKeys = {
                @ForeignKey(entity = Playlist.class, parentColumns = "id", childColumns = "playlistId"),
                @ForeignKey(entity = Song.class, parentColumns = "id", childColumns = "songId")
        }
)
public class PlaylistSongJoin {
    public int playlistId;
    public int songId;

    public PlaylistSongJoin(int playlistId, int songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }
}
