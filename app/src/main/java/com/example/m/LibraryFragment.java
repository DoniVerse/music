package com.example.m;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LibraryFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private LibraryPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_slider, container, false);
        
        // Initialize views
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        
        // Setup ViewPager2
        pagerAdapter = new LibraryPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        
        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Videos");
                    break;
                case 1:
                    tab.setText("Songs");
                    break;
                case 2:
                    tab.setText("Playlists");
                    break;
                case 3:
                    tab.setText("Artists");
                    break;
                case 4:
                    tab.setText("Albums");
                    break;
            }
        }).attach();
        
        return view;
    }
}
