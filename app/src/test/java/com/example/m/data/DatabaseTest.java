package com.example.m.data;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Looper;

import androidx.room.Room;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.example.m.data.dao.AlbumDao;
import com.example.m.data.dao.PlaybackHistoryDao;
import com.example.m.data.dao.PlaylistDao;
import com.example.m.data.dao.VideoDao;
import com.example.m.data.entities.Album;
import com.example.m.data.entities.PlaybackHistory;
import com.example.m.data.entities.Playlist;
import com.example.m.data.entities.Video;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(JUnit4.class)
public class DatabaseTest {
    private AppDatabase db;
    private AlbumDao albumDao;
    private VideoDao videoDao;
    private PlaylistDao playlistDao;
    private PlaybackHistoryDao playbackHistoryDao;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(
                new org.robolectric.RobolectricTestRunner.Config.Builder()
                        .build()
                        .application(),
                AppDatabase.class)
                .allowMainThreadQueries() // Only for testing
                .build();
        albumDao = db.albumDao();
        videoDao = db.videoDao();
        playlistDao = db.playlistDao();
        playbackHistoryDao = db.playbackHistoryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void testAlbumDaoOperations() {
        // Insert
        Album album1 = new Album();
        album1.setArtistId(1);
        album1.setTitle("Test Album 1");
        album1.setCoverUrl("https://test.com/cover1.jpg");
        long albumId1 = albumDao.insert(album1);

        Album album2 = new Album();
        album2.setArtistId(1);
        album2.setTitle("Test Album 2");
        album2.setCoverUrl("https://test.com/cover2.jpg");
        long albumId2 = albumDao.insert(album2);

        // Query
        List<Album> albums = albumDao.findByArtistId(1);
        assertEquals(2, albums.size());
        assertTrue(albums.stream().anyMatch(a -> a.getTitle().equals("Test Album 1")));
        assertTrue(albums.stream().anyMatch(a -> a.getTitle().equals("Test Album 2")));

        // Update
        Album updatedAlbum = album1;
        updatedAlbum.setTitle("Updated Album Title");
        albumDao.update(updatedAlbum);

        Album result = albumDao.findByArtistId(1).get(0);
        assertEquals("Updated Album Title", result.getTitle());

        // Delete
        albumDao.delete(album1);
        albums = albumDao.findByArtistId(1);
        assertEquals(1, albums.size());
    }

    @Test
    public void testVideoDaoOperations() {
        // Insert
        Video video1 = new Video();
        video1.setUserId(1);
        video1.setTitle("Test Video 1");
        video1.setUrl("https://test.com/video1.mp4");
        long videoId1 = videoDao.insert(video1);

        Video video2 = new Video();
        video2.setUserId(1);
        video2.setTitle("Test Video 2");
        video2.setUrl("https://test.com/video2.mp4");
        long videoId2 = videoDao.insert(video2);

        // Query
        List<Video> videos = videoDao.findByUserId(1);
        assertEquals(2, videos.size());
        assertTrue(videos.stream().anyMatch(v -> v.getTitle().equals("Test Video 1")));
        assertTrue(videos.stream().anyMatch(v -> v.getTitle().equals("Test Video 2")));

        // Update
        Video updatedVideo = video1;
        updatedVideo.setTitle("Updated Video Title");
        videoDao.update(updatedVideo);

        Video result = videoDao.findByUserId(1).get(0);
        assertEquals("Updated Video Title", result.getTitle());

        // Delete
        videoDao.delete(video1);
        videos = videoDao.findByUserId(1);
        assertEquals(1, videos.size());
    }

    @Test
    public void testPlaylistDaoOperations() {
        // Insert
        Playlist playlist1 = new Playlist();
        playlist1.setUserId(1);
        playlist1.setTitle("Test Playlist 1");
        playlist1.setDescription("Test description");
        long playlistId1 = playlistDao.insert(playlist1);

        Playlist playlist2 = new Playlist();
        playlist2.setUserId(1);
        playlist2.setTitle("Test Playlist 2");
        playlist2.setDescription("Another test playlist");
        long playlistId2 = playlistDao.insert(playlist2);

        // Query
        List<Playlist> playlists = playlistDao.findByUserId(1);
        assertEquals(2, playlists.size());
        assertTrue(playlists.stream().anyMatch(p -> p.getTitle().equals("Test Playlist 1")));
        assertTrue(playlists.stream().anyMatch(p -> p.getTitle().equals("Test Playlist 2")));

        // Update
        Playlist updatedPlaylist = playlist1;
        updatedPlaylist.setTitle("Updated Playlist Title");
        playlistDao.update(updatedPlaylist);

        Playlist result = playlistDao.findByUserId(1).get(0);
        assertEquals("Updated Playlist Title", result.getTitle());

        // Delete
        playlistDao.delete(playlist1);
        playlists = playlistDao.findByUserId(1);
        assertEquals(1, playlists.size());
    }

    @Test
    public void testPlaybackHistoryDaoOperations() {
        // Insert
        PlaybackHistory history1 = new PlaybackHistory();
        history1.setSongId(1);
        history1.setPlayedAt(System.currentTimeMillis());
        long historyId1 = playbackHistoryDao.insert(history1);

        PlaybackHistory history2 = new PlaybackHistory();
        history2.setSongId(1);
        history2.setPlayedAt(System.currentTimeMillis());
        long historyId2 = playbackHistoryDao.insert(history2);

        // Query by song ID
        List<PlaybackHistory> histories = playbackHistoryDao.findBySongId(1);
        assertEquals(2, histories.size());

        // Query recent history
        List<PlaybackHistory> recentHistory = playbackHistoryDao.getRecentHistory();
        assertEquals(2, recentHistory.size());

        // Delete
        playbackHistoryDao.delete(history1);
        histories = playbackHistoryDao.findBySongId(1);
        assertEquals(1, histories.size());
    }
}
