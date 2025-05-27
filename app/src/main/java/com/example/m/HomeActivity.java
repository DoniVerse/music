package com.example.m;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HomeActivity extends AppCompatActivity implements TrackAdapter.OnTrackClickListener {
    private ApiInterface apiInterface;
    private SearchView searchView;
    private RecyclerView artistsRecyclerView;
    private RecyclerView tracksRecyclerView;
    private TrackAdapter trackAdapter;
    private View searchResultsLayout;
    private CircularProfileView profileView;
    private TextView welcomeText;
    private String username;
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