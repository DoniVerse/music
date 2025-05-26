package com.example.music2;

import android.content.Context;

import com.example.music2.data.dao.PlaylistDao;
import com.example.music2.data.dao.SongDao;
import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.Playlist;
import com.example.music2.database.Song;

import java.util.List;

public class SystemPlaylistHelper {

    public static void generateSystemPlaylists(Context context, long userId) {
        AppDatabase db = AppDatabase.getInstance(context);
        PlaylistDao playlistDao = db.playlistDao();
        SongDao songDao = db.songDao();

        List<Playlist> playlists = playlistDao.getPlaylistsByUserNow(userId); // Synchronous method

        Playlist mostPlayed = getOrCreatePlaylist(playlistDao, userId, "Most Played");
        Playlist recentlyPlayed = getOrCreatePlaylist(playlistDao, userId, "Recently Played");

        List<Song> topPlayed = songDao.getMostPlayedSongs();
        List<Song> recent = songDao.getRecentlyPlayedSongs();

        db.playlistSongJoinDao().replaceSongsInPlaylist(mostPlayed.getId(), topPlayed);
        db.playlistSongJoinDao().replaceSongsInPlaylist(recentlyPlayed.getId(), recent);
    }

    private static Playlist getOrCreatePlaylist(PlaylistDao playlistDao, long userId, String name) {
        Playlist existing = playlistDao.getSystemPlaylistByName(userId, name);
        if (existing == null) {
            Playlist newPlaylist = new Playlist(name, name + " tracks", userId);
            newPlaylist.setSystemPlaylist(true);
            long id = playlistDao.insert(newPlaylist);
            newPlaylist.setId((int) id);
            return newPlaylist;
        }
        return existing;
    }
}
