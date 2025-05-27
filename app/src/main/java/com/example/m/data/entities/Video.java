package com.example.m.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "videos",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ))
public class Video {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private String filePath;
    private int duration;
    private String thumbnailPath;
    private int userId;

    public Video(String title, String filePath, int duration, int userId) {
        this.title = title;
        this.filePath = filePath;
        this.duration = duration;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getThumbnailPath() { return thumbnailPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
