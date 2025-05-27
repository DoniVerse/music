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
import com.example.m.data.dao.AlbumDao;
import com.example.m.data.dao.ArtistDao;

public class AlbumsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private AlbumDao albumDao;
    private ArtistDao artistDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        
        // Initialize DAOs
        albumDao = AppDatabase.getInstance(getContext()).albumDao();
        artistDao = AppDatabase.getInstance(getContext()).artistDao();
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        albumAdapter = new AlbumAdapter(albumDao, artistDao);
        recyclerView.setAdapter(albumAdapter);
        
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
        albumAdapter = null;
        albumDao = null;
        artistDao = null;
    }
}
