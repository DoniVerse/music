package com.example.music2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.music2.Adapter.SliderAdapter;
import com.example.music2.views.MusicListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private SliderAdapter adapter;
    private Button btnLogout;

    public static final String PREFS_NAME = "music_prefs";
    public static final String KEY_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1L);

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        viewPager = findViewById(R.id.viewPager);
        adapter = new SliderAdapter(this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Videos"); break;
                case 1: tab.setText("Songs"); break;
                case 2: tab.setText("Playlists"); break;
                case 3: tab.setText("Artists"); break;
                case 4: tab.setText("Albums"); break;
                case 5: tab.setText("Downloads"); break;
            }
        }).attach();

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            prefs.edit().remove(KEY_USER_ID).apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // BottomNavigationView setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Navigate to MusicListActivity
                startActivity(new Intent(MainActivity.this, MusicListActivity.class));
                return true;
            } else if (itemId == R.id.nav_local) {
                // You're already in MainActivity
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            }
            return false;
        });
    }
}