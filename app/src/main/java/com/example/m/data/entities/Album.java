package com.example.m.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "albums",
        foreignKeys = @ForeignKey(
                entity = Artist.class,
                parentColumns = "id",
                childColumns = "artistId",
                onDelete = ForeignKey.CASCADE
        ))
public class Album {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private int artistId;
    private String releaseDate;
    private String coverImagePath;

    public Album(String title, int artistId) {
        this.title = title;
        this.artistId = artistId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public String getCoverImagePath() { return coverImagePath; }
    public void setCoverImagePath(String coverImagePath) { this.coverImagePath = coverImagePath; }
}
