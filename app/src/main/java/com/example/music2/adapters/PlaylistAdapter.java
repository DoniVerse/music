package com.example.music2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.music2.R;
import com.example.music2.database.Playlist;

import java.util.List;

import lombok.NonNull;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private Context context;
    private List<Playlist> playlists;
    private OnPlaylistClickListener listener;

    public interface OnPlaylistClickListener {
        void onPlaylistClick(Playlist playlist);
    }

    public PlaylistAdapter(Context context, List<Playlist> playlists, OnPlaylistClickListener listener) {
        this.context = context;
        this.playlists = playlists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.tvPlaylistName.setText(playlist.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPlaylistClick(playlist);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }
    public void updateData(List<Playlist> newPlaylists) {
        this.playlists.clear();
        this.playlists.addAll(newPlaylists);
        notifyDataSetChanged();
    }


    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaylistName;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaylistName = itemView.findViewById(R.id.tvPlaylistName);
        }
    }
}
