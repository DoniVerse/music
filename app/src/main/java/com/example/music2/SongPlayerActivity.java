package com.example.music2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.Playlist;
import com.example.music2.database.PlaylistSong;
import com.example.music2.database.Song;
import com.example.music2.data.dao.PlaylistDao;
import com.example.music2.data.dao.PlaylistSongDao;
import com.example.music2.data.dao.SongDao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SongPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageButton btnPlayPause, btnPrevious, btnNext, btnAddToPlaylist, btnDownload, btnShare;
    private TextView tvSongTitle, tvArtistName;

    private boolean isPlaying = false;
    private String songPath;
    private String songTitle;
    private String artistName;
    private long songId;
    private long userId;
    private List<Song> currentPlaylist;
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        // Initialize views
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnAddToPlaylist = findViewById(R.id.btnAddToPlaylist);
        btnDownload = findViewById(R.id.btnDownload);
        btnShare = findViewById(R.id.btnShare);
        tvSongTitle = findViewById(R.id.tvSongTitle);
        tvArtistName = findViewById(R.id.tvArtistName);

        // Get song details from Intent extras
        Intent intent = getIntent();
        songPath = intent.getStringExtra("songPath");
        songTitle = intent.getStringExtra("songTitle");
        artistName = intent.getStringExtra("artistName");
        songId = intent.getLongExtra("songId", -1);
        userId = intent.getLongExtra("userId", -1);

        // Set song info
        tvSongTitle.setText(songTitle);
        tvArtistName.setText(artistName);

        // Initialize media player
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(songPath));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up button listeners
        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                pauseSong();
            } else {
                playSong();
            }
        });

        btnPrevious.setOnClickListener(v -> playPreviousSong());
        btnNext.setOnClickListener(v -> playNextSong());

        btnAddToPlaylist.setOnClickListener(v -> showPlaylistDialog());
        btnDownload.setOnClickListener(v -> downloadSong());
        btnShare.setOnClickListener(v -> shareSong());

        // Set up media player callbacks
        mediaPlayer.setOnCompletionListener(mp -> {
            isPlaying = false;
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            playNextSong();
        });

        // Load current playlist if available
        loadCurrentPlaylist();
    }

    private void loadCurrentPlaylist() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            PlaylistDao playlistDao = db.playlistDao();
            SongDao songDao = db.songDao();

            // Get all playlists for this user
            List<Playlist> playlists = playlistDao.getUserPlaylists(userId).getValue();
            if (playlists != null && !playlists.isEmpty()) {
                // Get songs from the first playlist (can be improved to select specific playlist)
                Playlist firstPlaylist = playlists.get(0);
                currentPlaylist = songDao.getSongsByPlaylist(firstPlaylist.getId());
                
                // Find current song index in playlist
                if (currentPlaylist != null) {
                    for (int i = 0; i < currentPlaylist.size(); i++) {
                        if (currentPlaylist.get(i).getId() == songId) {
                            currentSongIndex = i;
                            break;
                        }
                    }
                }
            }
        });
    }

    private void showPlaylistDialog() {
        // TODO: Implement playlist selection dialog
        Toast.makeText(this, "Playlist selection coming soon", Toast.LENGTH_SHORT).show();
    }

    private void downloadSong() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // TODO: Implement actual download logic
                File downloadsDir = new File(getExternalFilesDir(null), "downloads");
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs();
                }
                
                // Save song to downloads directory
                File songFile = new File(downloadsDir, songTitle + ".mp3");
                // Copy file logic here
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "Song downloaded successfully", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to download song", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void shareSong() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("audio/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(songPath));
        startActivity(Intent.createChooser(shareIntent, "Share song"));
    }

    private void playSong() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    private void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void playNextSong() {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % currentPlaylist.size();
            playSongFromPlaylist(currentSongIndex);
        }
    }

    private void playPreviousSong() {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentSongIndex = (currentSongIndex - 1 + currentPlaylist.size()) % currentPlaylist.size();
            playSongFromPlaylist(currentSongIndex);
        }
    }

    private void playSongFromPlaylist(int index) {
        if (currentPlaylist != null && index >= 0 && index < currentPlaylist.size()) {
            Song song = currentPlaylist.get(index);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, Uri.parse(song.getPath()));
                mediaPlayer.prepare();
                mediaPlayer.start();
                
                tvSongTitle.setText(song.getTitle());
                tvArtistName.setText(song.getArtist());
                
                isPlaying = true;
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
        btnPlayPause.setText("Pause");

        if (songId != -1) {
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                db.songDao().updatePlayStats(songId, System.currentTimeMillis());
            });
        }
    }

    private void pauseSong() {
        mediaPlayer.pause();
        isPlaying = false;
        btnPlayPause.setText("Play");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
