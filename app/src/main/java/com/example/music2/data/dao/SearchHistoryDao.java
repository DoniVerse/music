package com.example.music2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.music2.database.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {
    @Insert
    long insert(SearchHistory history);

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    List<SearchHistory> getRecentSearches();

    @Query("DELETE FROM search_history WHERE id = :id")
    void delete(long id);

    @Query("DELETE FROM search_history")
    void deleteAll();
}
