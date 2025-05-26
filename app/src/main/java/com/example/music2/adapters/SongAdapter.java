package com.example.music2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.music2.R;
import com.example.music2.SongPlayerActivity;
import com.example.music2.database.Song;

import java.util.List;

import lombok.NonNull;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private List<Song> songList;

    public SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.titleText.setText(song.getTitle());
        holder.artistText.setText("Artist ID: " + song.getArtistId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SongPlayerActivity.class);
            intent.putExtra("songId", song.getId()); // ✅ Add the songId
            intent.putExtra("songPath", song.getPath());
            intent.putExtra("songTitle", song.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, artistText;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.song_title);
            artistText = itemView.findViewById(R.id.song_artist);
        }
    }
}
