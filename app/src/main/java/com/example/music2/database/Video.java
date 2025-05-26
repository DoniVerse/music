package com.example.music2.database;



import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import android.net.Uri;

@Entity(tableName = "videos")
public class Video {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "duration")
    private long duration;

    @ColumnInfo(name = "size")
    private long size;

    @ColumnInfo(name = "uri")
    private String uri;  // Stored as String in DB

    @ColumnInfo(name = "userId")
    private long userId;  // Foreign key linking to User table

    // No-argument constructor (required by Room)
    public Video() {
    }

    // Constructor with all fields (except id)
    public Video(String name, long duration, long size, String uri, long userId) {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.uri = uri;
        this.userId = userId;
    }

    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    // This getter returns the raw String for Room
    public String getUri() {
        return uri;
    }

    // Overloaded setter for uri as String (Room uses this)
    public void setUri(String uri) {
        this.uri = uri;
    }

    // Overloaded setter for uri as Uri object (convenience)
    public void setUri(Uri uri) {
        this.uri = uri.toString();
    }

    // Convenience getter to get Uri object from string
    public Uri getUriObject() {
        return Uri.parse(uri);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
