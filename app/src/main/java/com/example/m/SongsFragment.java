package com.example.m;

import android.content.Context;
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

public class SongsFragment extends Fragment {
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private SongDao songDao;
    private ArtistDao artistDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        
        // Initialize DAOs
        Context context = getContext();
        if (context != null) {
            songDao = AppDatabase.getInstance(context).songDao();
            artistDao = AppDatabase.getInstance(context).artistDao();
            
            // Setup RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            songAdapter = new SongAdapter(songDao, artistDao);
            recyclerView.setAdapter(songAdapter);
        }
        
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
        songAdapter = null;
        songDao = null;
        artistDao = null;
    }
}
