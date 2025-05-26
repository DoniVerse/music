package com.example.music2.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;



@Entity(
        tableName = "songs",
        foreignKeys = @ForeignKey(
                entity = Artist.class,
                parentColumns = "id",
                childColumns = "artistId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("artistId")}
)
public class Song implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String path;

    @ColumnInfo(name = "artistId")
    private int artistId;

    @ColumnInfo(name = "lastPlayed", defaultValue = "0")
    private long lastPlayed;

    @ColumnInfo(name = "playCount", defaultValue = "0")
    private int playCount;

    @ColumnInfo(name = "lastPlayedTimestamp", defaultValue = "0")
    private long lastPlayedTimestamp;

    // No-arg constructor (useful for Room if needed)
    public Song() {
    }

    // Constructor
    public Song(String title, String path, int artistId) {
        this.title = title;
        this.path = path;
        this.artistId = artistId;
        this.lastPlayed = 0;
        this.playCount = 0;
        this.lastPlayedTimestamp = 0;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    public long getLastPlayed() { return lastPlayed; }
    public void setLastPlayed(long lastPlayed) { this.lastPlayed = lastPlayed; }

    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }

    public long getLastPlayedTimestamp() { return lastPlayedTimestamp; }
    public void setLastPlayedTimestamp(long lastPlayedTimestamp) { this.lastPlayedTimestamp = lastPlayedTimestamp; }

    // Parcelable Implementation
    protected Song(Parcel in) {
        id = in.readInt();
        title = in.readString();
        path = in.readString();
        artistId = in.readInt();
        lastPlayed = in.readLong();
        playCount = in.readInt();
        lastPlayedTimestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeInt(artistId);
        dest.writeLong(lastPlayed);
        dest.writeInt(playCount);
        dest.writeLong(lastPlayedTimestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
