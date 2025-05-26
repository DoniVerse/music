package com.example.music2;



import android.content.Context;

import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.Playlist;
import com.example.music2.data.dao.PlaylistDao;

import java.util.List;
import java.util.concurrent.Executors;

public class PlaylistUtils {

    public static void ensureSystemPlaylistsExist(Context context, long userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            PlaylistDao playlistDao = db.playlistDao();

            List<Playlist> systemPlaylists = playlistDao.getSystemPlaylistsSync(userId);

            boolean hasRecentlyPlayed = false;
            boolean hasMostPlayed = false;

            for (Playlist p : systemPlaylists) {
                if ("Recently Played".equals(p.getName())) hasRecentlyPlayed = true;
                if ("Most Played".equals(p.getName())) hasMostPlayed = true;
            }

            if (!hasRecentlyPlayed) {
                Playlist recentlyPlayed = new Playlist();
                recentlyPlayed.setUserId(userId);
                recentlyPlayed.setName("Recently Played");
                recentlyPlayed.setSystemPlaylist(true);
                playlistDao.insert(recentlyPlayed);

            }

            if (!hasMostPlayed) {
                Playlist mostPlayed = new Playlist();
                mostPlayed.setUserId(userId);
                mostPlayed.setName("Most Played");
                mostPlayed.setSystemPlaylist(true);
                playlistDao.insert(mostPlayed);
            }
        });
    }
}
