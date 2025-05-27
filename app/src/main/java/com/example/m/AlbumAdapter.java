package com.example.m;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m.data.dao.AlbumDao;
import com.example.m.data.dao.ArtistDao;
import com.example.m.data.entities.Album;
import com.example.m.data.entities.Artist;
import java.util.List;

public class AlbumAdapter extends MediaAdapter {
    private List<Album> albums;
    private AlbumDao albumDao;
    private ArtistDao artistDao;

    public AlbumAdapter(AlbumDao albumDao, ArtistDao artistDao) {
        this.albumDao = albumDao;
        this.artistDao = artistDao;
        loadAlbumsInBackground();
    }

    private void loadAlbumsInBackground() {
        new AsyncTask<Void, Void, List<Album>>() {
            @Override
            protected List<Album> doInBackground(Void... voids) {
                return albumDao.getAllAlbums();
            }

            @Override
            protected void onPostExecute(List<Album> albums) {
                AlbumAdapter.this.albums = albums;
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void bindMediaItem(MediaViewHolder holder, int position) {
        Album album = albums.get(position);
        
        // Load album cover image
        holder.mediaImage.setImageResource(R.drawable.ic_album);
        
        // Set album title
        holder.mediaTitle.setText(album.getTitle());
        
        // Get and set artist name as subtitle
        new AsyncTask<Void, Void, Artist>() {
            @Override
            protected Artist doInBackground(Void... voids) {
                return artistDao.findById(album.getArtistId());
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
        return albums != null ? albums.size() : 0;
    }
}
