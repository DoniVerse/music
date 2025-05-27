package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.PlaybackHistory;
import java.util.List;

@Dao
public interface PlaybackHistoryDao {
    @Insert
    long insert(PlaybackHistory playbackHistory);

    @Query("SELECT * FROM playback_history WHERE songId = :songId")
    List<PlaybackHistory> findBySongId(int songId);

    @Query("SELECT * FROM playback_history ORDER BY playedAt DESC LIMIT 10")
    List<PlaybackHistory> getRecentHistory();
}
