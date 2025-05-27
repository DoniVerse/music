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
import com.example.m.data.dao.ArtistDao;
import com.example.m.data.dao.SongDao;

public class ArtistsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private ArtistDao artistDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        
        // Initialize DAOs
        artistDao = AppDatabase.getInstance(getContext()).artistDao();
        SongDao songDao = AppDatabase.getInstance(getContext()).songDao();
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        artistAdapter = new ArtistAdapter(artistDao, songDao);
        recyclerView.setAdapter(artistAdapter);
        
        return view;
    }
}
