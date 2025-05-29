package com.example.m;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HomeActivity extends BottomNavigationActivity implements TrackAdapter.OnTrackClickListener {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_EMAIL = "user_email";
    private ApiInterface apiInterface;
    private SearchView searchView;
    private RecyclerView artistsRecyclerView;
    private RecyclerView tracksRecyclerView;
    private TrackAdapter trackAdapter;
    private View searchResultsLayout;
    private CircularProfileView profileView;
    private TextView welcomeText;
    private String userEmail;
    private String username;
    private BottomNavigationView bottomNavigationView;
    private final List<ApiArtist> popularArtists = Arrays.asList(
            createArtist("Eminem", "https://e-cdns-images.dzcdn.net/images/artist/19cc38f9d69b352f718782e7a22f9c32/250x250-000000-80-0-0.jpg"),
            createArtist("Ed Sheeran", "https://e-cdns-images.dzcdn.net/images/artist/2a03401e091893ec8abd8f15426b1147/250x250-000000-80-0-0.jpg"),
            createArtist("Taylor Swift", "https://e-cdns-images.dzcdn.net/images/artist/8e45f6d855d66828fa80bc9bbb4935ae/250x250-000000-80-0-0.jpg"),
            createArtist("Drake", "https://e-cdns-images.dzcdn.net/images/artist/5d2fa7f140a6bdc2c864c3465a61fc71/250x250-000000-80-0-0.jpg"),
            createArtist("The Weeknd", "https://e-cdns-images.dzcdn.net/images/artist/033d460f704896c9caca89a1d753a137/250x250-000000-80-0-0.jpg"),
            createArtist("Rihanna", "https://e-cdns-images.dzcdn.net/images/artist/7d514d87a186c02657a8e88a84de36f2/250x250-000000-80-0-0.jpg")
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        
        // Initialize views
        searchView = findViewById(R.id.searchView);
        artistsRecyclerView = findViewById(R.id.artistsRecyclerView);
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView);
        searchResultsLayout = findViewById(R.id.searchResultsLayout);
        welcomeText = findViewById(R.id.welcomeText);
        profileView = findViewById(R.id.profileView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        
        // Get user email from shared preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userEmail = prefs.getString(KEY_EMAIL, "");
        
        // Set profile circle letter
        if (!userEmail.isEmpty()) {
            String firstLetter = userEmail.substring(0, 1).toUpperCase();
            profileView.setLetter(firstLetter);
            
            // Set welcome text

            welcomeText.animate().alpha(1f).setDuration(500);
        }
        
        // Setup profile circle click
        profileView.setOnClickListener(v -> showProfileMenu(v));
        
        // Setup bottom navigation
        setupBottomNavigation();
        
        // Setup API and RecyclerViews
        setupDeezerApi();
        setupRecyclerViews();
        
        // Setup search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    searchTracks(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Navigate to HomeFragment
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFrame, new HomeFragment())
                    .commit();
                return true;
            } else if (item.getItemId() == R.id.navigation_library) {
                // Navigate to LibraryFragment
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFrame, new LibraryFragment())
                    .commit();
                return true;
            } else if (item.getItemId() == R.id.navigation_create) {
                showCreateMenu();
                return true;
            }
            return false;
        });
        
        // Set default selected item
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
    
    private void showCreateMenu() {
        PopupMenu popupMenu = new PopupMenu(this, bottomNavigationView);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.create_popup_menu, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.create_playlist) {
                showCreatePlaylistDialog();
                return true;
            } else if (item.getItemId() == R.id.create_album) {
                showCreateAlbumDialog();
                return true;
            } else if (item.getItemId() == R.id.create_station) {
                showCreateStationDialog();
                return true;
            }
            return false;
        });
        
        popupMenu.show();
    }
    
    private void showCreatePlaylistDialog() {
        CreatePlaylistDialogFragment dialog = new CreatePlaylistDialogFragment();
        dialog.show(getSupportFragmentManager(), "create_playlist_dialog");
    }
    
    private void showCreateAlbumDialog() {
        // TODO: Implement album creation dialog
        Toast.makeText(this, "Create Album coming soon!", Toast.LENGTH_SHORT).show();
    }
    
    private void showCreateStationDialog() {
        // TODO: Implement station creation dialog
        Toast.makeText(this, "Create Station coming soon!", Toast.LENGTH_SHORT).show();
    }
    
    private void showProfileMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_logout) {
                logout();
                return true;
            }
            return false;
        });
        
        popupMenu.show();
    }
    private void logout() {
        // Clear user session
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        
        // Navigate back to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void setupDeezerApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }
    private ApiArtist createArtist(String name, String pictureUrl) {
        ApiArtist artist = new ApiArtist();
        artist.setName(name);
        artist.setPictureMedium(pictureUrl);
        return artist;
    }
    private void setupRecyclerViews() {
        // Setup Artists RecyclerView
        artistsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ApiArtistAdapter artistAdapter = new ApiArtistAdapter(popularArtists, artist -> searchTracks(artist.getName()));
        artistsRecyclerView.setAdapter(artistAdapter);

        // Setup Tracks RecyclerView
        tracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trackAdapter = new TrackAdapter(new ArrayList<>(), this);
        tracksRecyclerView.setAdapter(trackAdapter);
    }


    private void searchTracks(String query) {
        artistsRecyclerView.setVisibility(View.GONE);
        welcomeText.setVisibility(View.GONE);
        searchResultsLayout.setVisibility(View.VISIBLE);

        Call<mydata> retrofitData = apiInterface.searchTracks(query);
        retrofitData.enqueue(new Callback<mydata>() {
            @Override
            public void onResponse(Call<mydata> call, Response<mydata> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mydata data = response.body();
                    if (data.getData() != null && !data.getData().isEmpty()) {
                        trackAdapter.updateTracks(data.getData());
                        Log.d(TAG, "Found " + data.getData().size() + " tracks");
                    } else {
                        Toast.makeText(HomeActivity.this, "No tracks found", Toast.LENGTH_SHORT).show();
                        trackAdapter.updateTracks(new ArrayList<>());
                    }
                } else {
                    String errorMessage = "Error: " + response.code();
                    Log.e(TAG, errorMessage);
                    Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mydata> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
                Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onTrackClick(Track track) {
        if (track.getPreview() != null) {
            // Launch PlayerActivity
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("title", track.getTitle());
            intent.putExtra("artist", track.getArtist().getName());
            intent.putExtra("albumArtUrl", track.getAlbum().getCover());
            intent.putExtra("streamUrl", track.getPreview());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No preview available for this track", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        if (searchResultsLayout.getVisibility() == View.VISIBLE) {
            // If search results are showing, go back to artists view
            searchResultsLayout.setVisibility(View.GONE);
            artistsRecyclerView.setVisibility(View.VISIBLE);
            welcomeText.setVisibility(View.VISIBLE);
            searchView.setQuery("", false);
            searchView.clearFocus();
        } else {
            // If on main screen, minimize app instead of going back
            moveTaskToBack(true);
        }
    }

}