package com.example.m;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {
    private static final int VIEW_TYPE_MEDIA = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MEDIA) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_media, parent, false);
            return new MediaViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_empty, parent, false);
            return new MediaViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        if (getItemCount() == 0) {
            // Handle empty state
            return;
        }
        bindMediaItem(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemCount() == 0 ? VIEW_TYPE_EMPTY : VIEW_TYPE_MEDIA;
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView mediaImage;
        TextView mediaTitle;
        TextView mediaSubtitle;

        MediaViewHolder(View itemView) {
            super(itemView);
            mediaImage = itemView.findViewById(R.id.mediaImage);
            mediaTitle = itemView.findViewById(R.id.mediaTitle);
            mediaSubtitle = itemView.findViewById(R.id.mediaSubtitle);
        }
    }

    protected abstract void bindMediaItem(MediaViewHolder holder, int position);
}
