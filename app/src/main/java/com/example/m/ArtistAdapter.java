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
import java.util.List;

public class ArtistAdapter extends MediaAdapter {
    private List<Artist> artists;
    private ArtistDao artistDao;
    private SongDao songDao;

    public ArtistAdapter(ArtistDao artistDao, SongDao songDao) {
        this.artistDao = artistDao;
        this.songDao = songDao;
        loadArtistsInBackground();
    }

    private void loadArtistsInBackground() {
        new AsyncTask<Void, Void, List<Artist>>() {
            @Override
            protected List<Artist> doInBackground(Void... voids) {
                return artistDao.getAllArtists();
            }

            @Override
            protected void onPostExecute(List<Artist> artists) {
                ArtistAdapter.this.artists = artists;
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void bindMediaItem(MediaViewHolder holder, int position) {
        Artist artist = artists.get(position);
        
        // Load artist image
        holder.mediaImage.setImageResource(R.drawable.ic_artist);
        
        // Set artist name
        holder.mediaTitle.setText(artist.getName());
        
        // Get and set number of songs for this artist
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return songDao.getSongCountForArtist(artist.getId());
            }

            @Override
            protected void onPostExecute(Integer songCount) {
                holder.mediaSubtitle.setText(String.format("%d songs", songCount));
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return artists != null ? artists.size() : 0;
    }
}
