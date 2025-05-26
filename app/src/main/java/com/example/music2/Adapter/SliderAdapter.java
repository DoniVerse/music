package com.example.music2.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music2.Fragment.AlbumsFragment;
import com.example.music2.Fragment.ArtistsFragment;
import com.example.music2.Fragment.DownloadsFragment;
import com.example.music2.Fragment.PlaylistsFragment;
import com.example.music2.Fragment.SongsFragment;
import com.example.music2.Fragment.VideosFragment;

public class SliderAdapter extends FragmentStateAdapter {
    public SliderAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new VideosFragment();
            case 1: return new SongsFragment();
            case 2: return new PlaylistsFragment();
            case 3: return new ArtistsFragment();
            case 4: return new AlbumsFragment();
            case 5: return new DownloadsFragment();
            default: return new VideosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
