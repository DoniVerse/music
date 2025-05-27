package com.example.m.data;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.m.data.dao.AlbumDao;
import com.example.m.data.dao.ArtistDao;
import com.example.m.data.dao.PlaybackHistoryDao;
import com.example.m.data.dao.PlaylistDao;
import com.example.m.data.dao.PlaylistSongDao;
import com.example.m.data.dao.SongDao;
import com.example.m.data.dao.UserDao;
import com.example.m.data.dao.VideoDao;
import com.example.m.data.entities.Album;
import com.example.m.data.entities.Artist;
import com.example.m.data.entities.Playlist;
import com.example.m.data.entities.Song;
import com.example.m.data.entities.User;
import com.example.m.data.entities.Video;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static final String TAG = "DatabaseTest";
    private AppDatabase database;
    private UserDao userDao;
    private ArtistDao artistDao;
    private AlbumDao albumDao;
    private SongDao songDao;
    private PlaylistDao playlistDao;
    private PlaylistSongDao playlistSongDao;
    private PlaybackHistoryDao playbackHistoryDao;
    private VideoDao videoDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // Using an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // Only for testing
                .build();
        
        userDao = database.userDao();
        artistDao = database.artistDao();
        albumDao = database.albumDao();
        songDao = database.songDao();
        playlistDao = database.playlistDao();
        playlistSongDao = database.playlistSongDao();
        playbackHistoryDao = database.playbackHistoryDao();
        videoDao = database.videoDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void testUserOperations() {
        // Test user creation
        User user = new User("testUser", "test@example.com", "password123");
        long userId = userDao.insert(user);
        assertTrue(userId > 0);

        // Test user retrieval
        User retrievedUser = userDao.findByUsername("testUser");
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    public void testArtistOperations() {
        // Test artist creation
        Artist artist = new Artist("Test Artist");
        long artistId = artistDao.insert(artist);
        assertTrue(artistId > 0);

        // Test artist retrieval
        Artist retrievedArtist = artistDao.findByName("Test Artist");
        assertNotNull(retrievedArtist);
        assertEquals("Test Artist", retrievedArtist.getName());
    }

    @Test
    public void testAlbumOperations() {
        // Create artist first
        Artist artist = new Artist("Test Artist");
        long artistId = artistDao.insert(artist);

        // Create album
        Album album = new Album("Test Album", (int)artistId);
        long albumId = albumDao.insert(album);
        assertTrue(albumId > 0);

        // Test album retrieval
        Album retrievedAlbum = albumDao.findByArtistId((int)artistId).get(0);
        assertNotNull(retrievedAlbum);
        assertEquals("Test Album", retrievedAlbum.getTitle());
    }

    @Test
    public void testSongOperations() {
        // Create artist first
        Artist artist = new Artist("Test Artist");
        long artistId = artistDao.insert(artist);

        // Create song
        Song song = new Song("Test Song", 180, "path/to/test/song.mp3", (int)artistId);
        long songId = songDao.insert(song);
        assertTrue(songId > 0);

        // Test song retrieval
        Song retrievedSong = songDao.findByArtistId((int)artistId).get(0);
        assertNotNull(retrievedSong);
        assertEquals("Test Song", retrievedSong.getTitle());
    }

    @Test
    public void testPlaylistOperations() {
        // Create user first
        User user = new User("testUser", "test@example.com", "password123");
        long userId = userDao.insert(user);

        // Create playlist
        Playlist playlist = new Playlist("Test Playlist", (int)userId);
        long playlistId = playlistDao.insert(playlist);
        assertTrue(playlistId > 0);

        // Test playlist retrieval
        Playlist retrievedPlaylist = playlistDao.findByUserId((int)userId).get(0);
        assertNotNull(retrievedPlaylist);
        assertEquals("Test Playlist", retrievedPlaylist.getName());
    }

    @Test
    public void testVideoOperations() {
        // Create user first
        User user = new User("testUser", "test@example.com", "password123");
        long userId = userDao.insert(user);

        // Create video
        Video video = new Video("Test Video", "path/to/test/video.mp4", 300, (int)userId);
        long videoId = videoDao.insert(video);
        assertTrue(videoId > 0);

        // Test video retrieval
        Video retrievedVideo = videoDao.findByUserId((int)userId).get(0);
        assertNotNull(retrievedVideo);
        assertEquals("Test Video", retrievedVideo.getTitle());
    }
}
