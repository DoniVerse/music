package com.example.music2.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "playlists",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("userId")}
)
public class Playlist {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private boolean isSystemPlaylist;
    private String description;

    @ColumnInfo(name = "userId")
    private long userId;

    // ✅ No-argument constructor (REQUIRED by Room and Java)
    public Playlist() {}

    // ✅ Constructor for custom initialization
    public Playlist(String name, String description, long userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.isSystemPlaylist = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public boolean isSystemPlaylist() { return isSystemPlaylist; }
    public void setSystemPlaylist(boolean systemPlaylist) { isSystemPlaylist = systemPlaylist; }
}
