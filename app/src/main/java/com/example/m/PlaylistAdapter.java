package com.example.m;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m.data.dao.PlaylistDao;
import com.example.m.data.dao.SongDao;
import com.example.m.data.entities.Playlist;
import java.util.List;

public class PlaylistAdapter extends MediaAdapter {
    private List<Playlist> playlists;
    private PlaylistDao playlistDao;
    private SongDao songDao;

    public PlaylistAdapter(PlaylistDao playlistDao, SongDao songDao) {
        this.playlistDao = playlistDao;
        this.songDao = songDao;
        loadPlaylistsInBackground();
    }

    private void loadPlaylistsInBackground() {
        new AsyncTask<Void, Void, List<Playlist>>() {
            @Override
            protected List<Playlist> doInBackground(Void... voids) {
                return playlistDao.getAllPlaylists();
            }

            @Override
            protected void onPostExecute(List<Playlist> playlists) {
                PlaylistAdapter.this.playlists = playlists;
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void bindMediaItem(MediaViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        
        // Load playlist cover image
        holder.mediaImage.setImageResource(R.drawable.ic_playlist);
        
        // Set playlist name
        holder.mediaTitle.setText(playlist.getName());
        
        // Get and set number of songs for this playlist
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return songDao.getSongCountForPlaylist(playlist.getId());
            }

            @Override
            protected void onPostExecute(Integer songCount) {
                holder.mediaSubtitle.setText(String.format("%d songs", songCount));
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return playlists != null ? playlists.size() : 0;
    }
}
