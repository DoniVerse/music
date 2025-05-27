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
import com.example.m.data.dao.PlaylistDao;
import com.example.m.data.dao.SongDao;

public class PlaylistsFragment extends Fragment {
    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private PlaylistDao playlistDao;
    private SongDao songDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        
        // Initialize DAOs
        Context context = getContext();
        if (context != null) {
            playlistDao = AppDatabase.getInstance(context).playlistDao();
            songDao = AppDatabase.getInstance(context).songDao();
            
            // Setup RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            playlistAdapter = new PlaylistAdapter(playlistDao, songDao);
            recyclerView.setAdapter(playlistAdapter);
        }
        
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
        playlistAdapter = null;
        playlistDao = null;
        songDao = null;
    }
}
