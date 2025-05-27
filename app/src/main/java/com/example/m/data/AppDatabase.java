package com.example.m.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.m.data.dao.*;

@Database(entities = {
        com.example.m.data.entities.User.class,
        com.example.m.data.entities.Artist.class,
        com.example.m.data.entities.Album.class,
        com.example.m.data.entities.Song.class,
        com.example.m.data.entities.Playlist.class,
        com.example.m.data.entities.PlaylistSong.class,
        com.example.m.data.entities.PlaybackHistory.class,
        com.example.m.data.entities.Video.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract ArtistDao artistDao();
    public abstract AlbumDao albumDao();
    public abstract SongDao songDao();
    public abstract PlaylistDao playlistDao();
    public abstract PlaylistSongDao playlistSongDao();
    public abstract PlaybackHistoryDao playbackHistoryDao();
    public abstract VideoDao videoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "music_app_database"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
}
