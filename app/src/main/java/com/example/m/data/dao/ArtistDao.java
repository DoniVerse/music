package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.Artist;
import java.util.List;

@Dao
public interface ArtistDao {
    @Insert
    long insert(Artist artist);

    @Update
    void update(Artist artist);

    @Delete
    void delete(Artist artist);

    @Query("SELECT * FROM artists WHERE name = :name")
    Artist findByName(String name);

    @Query("SELECT * FROM artists")
    List<Artist> getAllArtists();
}
