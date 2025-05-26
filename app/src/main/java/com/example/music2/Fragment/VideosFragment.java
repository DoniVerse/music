package com.example.music2.Fragment;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music2.R;
import com.example.music2.SessionManager;
import com.example.music2.adapters.VideoAdapter;
import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.Video;

import java.util.ArrayList;
import java.util.List;
public class VideosFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewVideos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sessionManager = new SessionManager(requireContext());
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "music_db").allowMainThreadQueries().build();

        checkPermissionAndLoadVideos();

        return view;
    }

    private void checkPermissionAndLoadVideos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_VIDEO}, PERMISSION_REQUEST_CODE);
            } else {
                loadVideos();
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                loadVideos();
            }
        }
    }

    private void loadVideos() {
        videoList = new ArrayList<>();
        ContentResolver resolver = requireContext().getContentResolver();
        Uri collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };

        try (Cursor cursor = resolver.query(collection, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

                long userId = sessionManager.getUserId();

                do {
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    long duration = cursor.getLong(durationColumn);
                    long size = cursor.getLong(sizeColumn);
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                    Video video = new Video(name, duration, size, contentUri.toString(), userId);
                    videoList.add(video);
                    db.videoDao().insert(video);
                } while (cursor.moveToNext());
            }
        }

        adapter = new VideoAdapter(requireContext(), videoList);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideos();
            } else {
                Toast.makeText(getContext(), "Permission denied. Cannot load videos.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}