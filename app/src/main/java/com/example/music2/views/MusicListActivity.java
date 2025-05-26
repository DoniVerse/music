package com.example.music2.views;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music2.R;
import com.example.music2.models.APIArtist;
import com.example.music2.models.APITrackAdapter;
import com.example.music2.adapters.APIArtistAdapter;
import com.example.music2.models.APITrack;
import com.example.music2.models.ApiInterface;
import com.example.music2.models.mydata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MusicListActivity extends AppCompatActivity implements APITrackAdapter.OnTrackClickListener {

    private static final String TAG = "MusicListActivity";
    private SearchView searchView;
    private RecyclerView artistsRecyclerView;
    private RecyclerView tracksRecyclerView;
    private APITrackAdapter trackAdapter;
    private APIArtistAdapter artistAdapter;
    private View searchResultsLayout;
    private ApiInterface apiInterface;
    private CircularProfileView profileView;
    private TextView welcomeText;
    private String username;

    private final List<APIArtist> popularArtists = Arrays.asList(
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
        setContentView(R.layout.activity_music_list);

        initializeViews();
        setupRecyclerViews();
        setupClickListeners();
        setupDeezerApi();
//        setupProfileView();
        showWelcomeMessage();
    }

    private void initializeViews() {
        searchView = findViewById(R.id.searchView);
        artistsRecyclerView = findViewById(R.id.artistsRecyclerView);
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView);
        searchResultsLayout = findViewById(R.id.searchResultsLayout);
//        profileView = findViewById(R.id.profileView);
//        welcomeText = findViewById(R.id.welcomeText);
        username = getIntent().getStringExtra("USERNAME");
    }

    private void showWelcomeMessage() {
        if (username != null && !username.isEmpty()) {
            welcomeText.setText("Popular Artists");

            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(welcomeText, "alpha", 0f, 1f);
            fadeIn.setDuration(500);
            fadeIn.start();
        }
    }

//    private void setupProfileView() {
//        if (username != null && !username.isEmpty()) {
//            profileView.setLetter(username);
//            profileView.setOnClickListener(v -> showProfileDialog());
//        }
//    }

//    private void showProfileDialog() {
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_profile_details, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_BeatBop_Dialog);
//
//        TextView fullNameText = dialogView.findViewById(R.id.fullNameText);
//        TextView emailText = dialogView.findViewById(R.id.emailText);
//        MaterialButton logoutButton = dialogView.findViewById(R.id.logoutButton);
//
//        fullNameText.setText(username);
//        emailText.setText("user@example.com");
//
//        AlertDialog dialog = builder.setView(dialogView).create();
//
//        logoutButton.setOnClickListener(v -> {
//            dialog.dismiss();
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        });
//
//        dialog.show();
//    }

    private void setupRecyclerViews() {
        // Artists RecyclerView
        artistsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        artistAdapter = new APIArtistAdapter(popularArtists, artist -> searchTracks(artist.getName()));
        artistsRecyclerView.setAdapter(artistAdapter);

        // Tracks RecyclerView
        tracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trackAdapter = new APITrackAdapter(new ArrayList<>(), this);
        tracksRecyclerView.setAdapter(trackAdapter);
    }

    private void setupClickListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTracks(query);
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
                .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
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
                        Toast.makeText(MusicListActivity.this, "No tracks found", Toast.LENGTH_SHORT).show();
                        trackAdapter.updateTracks(new ArrayList<>());
                    }
                } else {
                    String errorMessage = "Error: " + response.code();
                    Log.e(TAG, errorMessage);
                    Toast.makeText(MusicListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mydata> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
                Toast.makeText(MusicListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTrackClick(APITrack track) {
        if (track.getPreview() != null) {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("title", track.getTitle());

            if (track.getArtist() != null) {
                intent.putExtra("artist", track.getArtist().getName());
            }

            if (track.getAlbum() != null) {
                intent.putExtra("albumArtUrl", track.getAlbum().getCover());
            }

            intent.putExtra("streamUrl", track.getPreview());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No preview available for this track", Toast.LENGTH_SHORT).show();
        }
    }

    private APIArtist createArtist(String name, String pictureUrl) {
        APIArtist artist = new APIArtist();
        artist.setName(name);
        artist.setPictureMedium(pictureUrl);
        return artist;
    }

    @Override
    public void onBackPressed() {
        if (searchResultsLayout.getVisibility() == View.VISIBLE) {
            searchResultsLayout.setVisibility(View.GONE);
            artistsRecyclerView.setVisibility(View.VISIBLE);
            welcomeText.setVisibility(View.VISIBLE);
            searchView.setQuery("", false);
            searchView.clearFocus();
        } else {
            moveTaskToBack(true);
        }
    }
}
