package com.example.m.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_songs",
        foreignKeys = {
            @ForeignKey(
                    entity = Playlist.class,
                    parentColumns = "id",
                    childColumns = "playlistId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Song.class,
                    parentColumns = "id",
                    childColumns = "songId",
                    onDelete = ForeignKey.CASCADE
            )
        })
public class PlaylistSong {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int playlistId;
    private int songId;

    public PlaylistSong(int playlistId, int songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }
    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }
}
