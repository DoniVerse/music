package com.example.music2.data.entity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.music2.data.dao.PlaylistDao;
import com.example.music2.data.dao.PlaylistSongDao;
import com.example.music2.data.dao.PlaylistSongJoinDao;
import com.example.music2.data.dao.SongDao;
import com.example.music2.data.dao.UserDao;
import com.example.music2.data.dao.VideoDao;
import com.example.music2.database.Artist;
import com.example.music2.database.Playlist;
import com.example.music2.database.PlaylistSong;
import com.example.music2.database.Song;
import com.example.music2.database.User;
import com.example.music2.database.Video;

@Database(entities = {User.class,Video.class ,Song.class, Playlist.class, PlaylistSong.class, Artist.class}, version = 3)


public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract VideoDao videoDao();
    public abstract SongDao songDao();

    public abstract PlaylistDao playlistDao();

    public abstract PlaylistSongDao playlistSongDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "music_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 👇 Add column to existing songs table
            database.execSQL("ALTER TABLE songs ADD COLUMN lastPlayedTimestamp INTEGER NOT NULL DEFAULT 0");
        }
    };

    public PlaylistSongJoinDao playlistSongJoinDao() {
        return null;
    }
}
