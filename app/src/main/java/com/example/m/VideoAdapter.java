package com.example.m;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m.data.dao.VideoDao;
import com.example.m.data.entities.Video;
import java.util.List;

public class VideoAdapter extends MediaAdapter {
    private List<Video> videos;
    private VideoDao videoDao;

    public VideoAdapter(VideoDao videoDao) {
        this.videoDao = videoDao;
        loadVideosInBackground();
    }

    private void loadVideosInBackground() {
        new AsyncTask<Void, Void, List<Video>>() {
            @Override
            protected List<Video> doInBackground(Void... voids) {
                return videoDao.getAllVideos();
            }

            @Override
            protected void onPostExecute(List<Video> videos) {
                VideoAdapter.this.videos = videos;
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void bindMediaItem(MediaViewHolder holder, int position) {
        Video video = videos.get(position);
        
        // Load video thumbnail
        if (video.getThumbnailPath() != null) {
            holder.mediaImage.setImageBitmap(BitmapFactory.decodeFile(video.getThumbnailPath()));
        } else {
            holder.mediaImage.setImageResource(R.drawable.ic_video);
        }
        
        // Set video title
        holder.mediaTitle.setText(video.getTitle());
        
        // Set duration as subtitle
        String durationString = String.format("%d:%02d", 
            video.getDuration() / 60, 
            video.getDuration() % 60);
        holder.mediaSubtitle.setText(durationString);
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }
}
