package com.example.music2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// adjust package accordingly

import android.net.Uri;

public class VideoItem {
    public long id;
    public String name;
    public long duration;
    public Uri contentUri;

    public VideoItem(long id, String name, long duration, Uri contentUri) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.contentUri = contentUri;
    }
}
