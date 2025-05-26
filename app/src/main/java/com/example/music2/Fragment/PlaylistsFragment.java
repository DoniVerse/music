package com.example.music2.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.music2.R;
import com.example.music2.SessionManager;
import com.example.music2.adapters.PlaylistAdapter;
import com.example.music2.data.entity.AppDatabase;
import com.example.music2.database.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsFragment extends Fragment {

    private RecyclerView recyclerViewSystem, recyclerViewUser;
    private PlaylistAdapter systemPlaylistAdapter, userPlaylistAdapter;
    private List<Playlist> systemPlaylists = new ArrayList<>();
    private List<Playlist> userPlaylists = new ArrayList<>();
    private AppDatabase db;
    private SessionManager sessionManager;
    private Button addPlaylistButton;

    private long userId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        recyclerViewSystem = view.findViewById(R.id.recyclerViewSystemPlaylists);
        recyclerViewUser = view.findViewById(R.id.recyclerViewUserPlaylists);
        addPlaylistButton = view.findViewById(R.id.buttonAddPlaylist);

        recyclerViewSystem.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        db = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        addPlaylistButton.setOnClickListener(v -> showAddPlaylistDialog());

        createSystemPlaylistsIfNeeded();

        loadUserPlaylists();

        return view;
    }

    private void createSystemPlaylistsIfNeeded() {
        // This runs once to ensure system playlists exist for this user

        // Query system playlists by userId (and isSystemPlaylist = true)
        db.playlistDao().getSystemPlaylistsByUser(userId).observe(getViewLifecycleOwner(), playlists -> {
            if (playlists == null || playlists.isEmpty()) {
                // Create system playlists
                Playlist mostPlayed = new Playlist("Most Played", "Auto-generated system playlist", userId);
                mostPlayed.setSystemPlaylist(true);

                Playlist recentlyPlayed = new Playlist("Recently Played", "Auto-generated system playlist", userId);
                recentlyPlayed.setSystemPlaylist(true);

                new Thread(() -> {
                    db.playlistDao().insert(mostPlayed);
                    db.playlistDao().insert(recentlyPlayed);
                }).start();
            } else {
                // Update UI with system playlists
                systemPlaylists = playlists;
                if (systemPlaylistAdapter == null) {
                    systemPlaylistAdapter = new PlaylistAdapter(getContext(), systemPlaylists, playlist -> {
                        Toast.makeText(getContext(), "Clicked system playlist: " + playlist.getName(), Toast.LENGTH_SHORT).show();
                        // TODO: open system playlist details or play
                    });
                    recyclerViewSystem.setAdapter(systemPlaylistAdapter);
                } else {
                    systemPlaylistAdapter.updateData(systemPlaylists);
                }
            }
        });
    }

    private void loadUserPlaylists() {
        // Load user-created playlists (non-system)
        db.playlistDao().getUserPlaylists(userId).observe(getViewLifecycleOwner(), playlists -> {
            userPlaylists = playlists;
            if (userPlaylistAdapter == null) {
                userPlaylistAdapter = new PlaylistAdapter(getContext(), userPlaylists, playlist -> {
                    Toast.makeText(getContext(), "Clicked playlist: " + playlist.getName(), Toast.LENGTH_SHORT).show();
                    // TODO: open playlist details or player activity here
                });
                recyclerViewUser.setAdapter(userPlaylistAdapter);
            } else {
                userPlaylistAdapter.updateData(userPlaylists);
            }
        });
    }

    private void showAddPlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create New Playlist");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(input);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String playlistName = input.getText().toString().trim();
            if (playlistName.isEmpty()) {
                Toast.makeText(getContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            createNewPlaylist(playlistName);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void createNewPlaylist(String name) {
        Playlist newPlaylist = new Playlist(name, "User created playlist", userId);
        newPlaylist.setSystemPlaylist(false);

        new Thread(() -> {
            long id = db.playlistDao().insert(newPlaylist);
            requireActivity().runOnUiThread(() -> {
                if (id > 0) {
                    Toast.makeText(getContext(), "Playlist created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to create playlist", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
