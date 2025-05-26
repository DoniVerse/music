package com.example.music2.adapters;




import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music2.R;
import com.example.music2.database.Video;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Video> videos;
    private Context context;

    public VideoAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        ExoPlayer player;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
        }

        void bind(Video video) {
            // Release any previous player
            if (player != null) {
                player.release();
            }

            // Create new player instance
            player = new ExoPlayer.Builder(context).build();
            playerView.setPlayer(player);

            Uri videoUri = Uri.parse(video.getUri());

            // Prepare media source
            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            player.setMediaItem(mediaItem);

            player.prepare();
            player.setPlayWhenReady(false);  // Don't autoplay; play on user interaction

            // Optional: set click listener to toggle play/pause
            playerView.setOnClickListener(v -> {
                if (player.isPlaying()) {
                    player.pause();
                } else {
                    player.play();
                }
            });
        }
    }

    // Release players when adapter is cleared to avoid memory leaks
    public void releasePlayers() {
        for (int i = 0; i < getItemCount(); i++) {
            // You might want to track players and release them here if stored.
        }
    }
}
