package com.example.music2.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music2.R;
import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.Song;
import com.example.music2.database.SearchHistory;
import com.example.music2.adapters.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private SearchView searchView;
    private List<Song> songs;
    private List<SearchHistory> searchHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerViewSongs);
        searchView = view.findViewById(R.id.searchView);
        
        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songs = new ArrayList<>();
        songAdapter = new SongAdapter(songs);
        recyclerView.setAdapter(songAdapter);
        
        // Load songs
        loadSongs();
        
        // Set up search
        setupSearch();
        
        return view;
    }

    private void loadSongs() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            
            // Load both local and API songs
            List<Song> localSongs = db.songDao().getAllLocalSongs();
            List<Song> apiSongs = db.songDao().getAllApiSongs();
            
            songs.clear();
            songs.addAll(localSongs);
            songs.addAll(apiSongs);
            
            runOnUiThread(() -> songAdapter.notifyDataSetChanged());
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                saveSearchHistory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText);
                return false;
            }
        });
    }

    private void saveSearchHistory(String query) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            SearchHistory history = new SearchHistory();
            history.setQuery(query);
            db.searchHistoryDao().insert(history);
        });
    }

    private void filterSongs(String query) {
        if (query.isEmpty()) {
            loadSongs();
            return;
        }

        List<Song> filteredSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                song.getArtist().toLowerCase().contains(query.toLowerCase())) {
                filteredSongs.add(song);
            }
        }

        songAdapter.updateSongs(filteredSongs);
    }
}
