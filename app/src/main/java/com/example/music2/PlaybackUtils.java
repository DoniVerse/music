package com.example.music2;



import android.content.Context;

import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.Playlist;
import com.example.music2.data.dao.PlaylistDao;
import com.example.music2.database.PlaylistSong;
import com.example.music2.data.dao.PlaylistSongDao;

import java.util.List;
import java.util.concurrent.Executors;

public class PlaybackUtils {

    public static void onSongPlayed(Context context, long userId, long songId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            PlaylistDao playlistDao = db.playlistDao();
            PlaylistSongDao playlistSongDao = db.playlistSongDao();

            List<Playlist> systemPlaylists = playlistDao.getSystemPlaylistsSync(userId);

            for (Playlist playlist : systemPlaylists) {
                PlaylistSong ps = playlistSongDao.getSongInPlaylist(playlist.getId(), songId);
                if (ps == null) {
                    ps = new PlaylistSong();
                    ps.setPlaylistId(playlist.getId());
                    ps.setSongId(songId);
                    ps.setLastPlayedTimestamp(System.currentTimeMillis());
                    ps.setPlayCount(1);
                    playlistSongDao.insert(ps);
                } else {
                    if ("Recently Played".equals(playlist.getName())) {
                        ps.setLastPlayedTimestamp(System.currentTimeMillis());
                    }
                    if ("Most Played".equals(playlist.getName())) {
                        ps.setPlayCount(ps.getPlayCount() + 1);
                    }
                    playlistSongDao.insert(ps);
                }
            }
        });
    }
}
