package com.example.m;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CreatePlaylistDialogFragment extends DialogFragment {
    private EditText playlistNameInput;
    private EditText playlistDescriptionInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_playlist, null);
        
        // Initialize views
        playlistNameInput = dialogView.findViewById(R.id.playlistNameInput);
        playlistDescriptionInput = dialogView.findViewById(R.id.playlistDescriptionInput);
        
        builder.setView(dialogView)
            .setTitle("Create Playlist")
            .setPositiveButton("Create", (dialog, id) -> {
                String playlistName = playlistNameInput.getText().toString().trim();
                String playlistDescription = playlistDescriptionInput.getText().toString().trim();
                
                if (playlistName.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a playlist name", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // TODO: Implement playlist creation logic
                Toast.makeText(getContext(), "Playlist created: " + playlistName, Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancel", (dialog, id) -> {
                // User cancelled the dialog
            });
        
        return builder.create();
    }
}
