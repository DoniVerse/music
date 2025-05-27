package com.example.m;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LibraryPagerAdapter extends FragmentStateAdapter {
    public LibraryPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new VideosFragment();
            case 1:
                return new SongsFragment();
            case 2:
                return new PlaylistsFragment();
            case 3:
                return new ArtistsFragment();
            case 4:
                return new AlbumsFragment();
            default:
                return new VideosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Videos, Songs, Playlists, Artists, Albums
    }
}
