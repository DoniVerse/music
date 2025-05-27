package com.example.m.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "playback_history",
        foreignKeys = @ForeignKey(
                entity = Song.class,
                parentColumns = "id",
                childColumns = "songId",
                onDelete = ForeignKey.CASCADE
        ))
public class PlaybackHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int songId;
    private String playedAt;

    public PlaybackHistory(int songId) {
        this.songId = songId;
        this.playedAt = new java.util.Date().toString();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }
    public String getPlayedAt() { return playedAt; }
    public void setPlayedAt(String playedAt) { this.playedAt = playedAt; }
}
