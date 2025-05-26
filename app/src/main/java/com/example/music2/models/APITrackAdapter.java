package com.example.music2.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class APITrackAdapter extends RecyclerView.Adapter<APITrackAdapter.TrackViewHolder> {
    private List<APITrack> tracks;
    private OnTrackClickListener listener;

    public interface OnTrackClickListener {
        void onTrackClick(APITrack track);
    }

    public APITrackAdapter(List<APITrack> tracks, OnTrackClickListener listener) {
        this.tracks = tracks;
        this.listener = listener;
    }

    public void updateTracks(List<APITrack> newTracks) {
        this.tracks = newTracks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        APITrack track = tracks.get(position);
        holder.titleView.setText(track.getTitle());

        APIArtist artist = track.getArtist();
        holder.artistView.setText(artist != null ? artist.getName() : "Unknown Artist");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTrackClick(track);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks != null ? tracks.size() : 0;
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView artistView;

        TrackViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(android.R.id.text1);
            artistView = itemView.findViewById(android.R.id.text2);
        }
    }
}

