package com.example.music2;

import android.content.Context;
import android.content.SharedPreferences;
public class SessionManager {
    private static final String PREF_NAME = "music_prefs";
    private static final String KEY_USER_ID = "userId";


    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserId(long userId) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply();
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1); // -1 means not logged in
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
