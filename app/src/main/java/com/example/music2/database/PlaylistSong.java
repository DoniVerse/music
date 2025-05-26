package com.example.music2.database;



import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "playlist_songs",
        primaryKeys = {"playlistId", "songId"},
        foreignKeys = {
                @ForeignKey(entity = Playlist.class, parentColumns = "id", childColumns = "playlistId"),
                @ForeignKey(entity = Song.class, parentColumns = "id", childColumns = "songId")
        })
public class PlaylistSong {
    private long playlistId;
    private long songId;

    private long lastPlayedTimestamp;
    private int playCount;

    public long getPlaylistId() { return playlistId; }
    public void setPlaylistId(long playlistId) { this.playlistId = playlistId; }

    public long getSongId() { return songId; }
    public void setSongId(long songId) { this.songId = songId; }

    public long getLastPlayedTimestamp() { return lastPlayedTimestamp; }
    public void setLastPlayedTimestamp(long lastPlayedTimestamp) { this.lastPlayedTimestamp = lastPlayedTimestamp; }

    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }
}
