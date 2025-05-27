package com.example.m;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m.data.dao.ArtistDao;
import com.example.m.data.dao.SongDao;
import com.example.m.data.entities.Artist;
import com.example.m.data.entities.Song;
import java.util.List;

public class SongAdapter extends MediaAdapter {
    private List<Song> songs;
    private SongDao songDao;
    private ArtistDao artistDao;

    public SongAdapter(SongDao songDao, ArtistDao artistDao) {
        this.songDao = songDao;
        this.artistDao = artistDao;
        loadSongsInBackground();
    }

    private void loadSongsInBackground() {
        new AsyncTask<Void, Void, List<Song>>() {
            @Override
            protected List<Song> doInBackground(Void... voids) {
                return songDao.getAllSongs();
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                SongAdapter.this.songs = songs;
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void bindMediaItem(MediaViewHolder holder, int position) {
        Song song = songs.get(position);
        
        // Load song cover image
        holder.mediaImage.setImageResource(R.drawable.ic_song);
        
        // Set song title
        holder.mediaTitle.setText(song.getTitle());
        
        // Get and set artist name as subtitle
        new AsyncTask<Void, Void, Artist>() {
            @Override
            protected Artist doInBackground(Void... voids) {
                return artistDao.findById(song.getArtistId());
            }

            @Override
            protected void onPostExecute(Artist artist) {
                if (artist != null) {
                    holder.mediaSubtitle.setText(artist.getName());
                } else {
                    holder.mediaSubtitle.setText("Unknown Artist");
                }
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return songs != null ? songs.size() : 0;
    }
}
