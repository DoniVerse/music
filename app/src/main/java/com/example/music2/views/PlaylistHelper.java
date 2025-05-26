package com.example.music2.views;

import android.content.Context;

import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.PlaylistSong;

public class PlaylistHelper {

    private final AppDatabase db;

    public PlaylistHelper(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public void addSongToPlaylist(int playlistId, long songId) {
        int count = db.playlistSongDao().isSongInPlaylist(playlistId, (int)songId);
        if (count == 0) {
            PlaylistSong ps = new PlaylistSong();
            ps.setPlaylistId(playlistId);
            ps.setSongId(songId);
            ps.setLastPlayedTimestamp(0);
            ps.setPlayCount(0);

            db.playlistSongDao().insert(ps);
        }
    }
}
