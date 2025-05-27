package com.example.m;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m.data.AppDatabase;
import com.example.m.data.dao.VideoDao;

public class VideosFragment extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private VideoDao videoDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        
        // Initialize DAO
        videoDao = AppDatabase.getInstance(getContext()).videoDao();
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        videoAdapter = new VideoAdapter(videoDao);
        recyclerView.setAdapter(videoAdapter);
        
        return view;
    }
}
