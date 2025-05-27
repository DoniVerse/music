package com.example.m.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "songs",
        foreignKeys = {
            @ForeignKey(
                    entity = Artist.class,
                    parentColumns = "id",
                    childColumns = "artistId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Album.class,
                    parentColumns = "id",
                    childColumns = "albumId",
                    onDelete = ForeignKey.SET_NULL
            )
        })
public class Song {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private int duration;
    private String filePath;
    private int artistId;
    private int albumId;
    private int playCount;
    private String lastPlayed;

    public Song(String title, int duration, String filePath, int artistId) {
        this.title = title;
        this.duration = duration;
        this.filePath = filePath;
        this.artistId = artistId;
        this.playCount = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }
    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }
    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }
    public String getLastPlayed() { return lastPlayed; }
    public void setLastPlayed(String lastPlayed) { this.lastPlayed = lastPlayed; }
}
