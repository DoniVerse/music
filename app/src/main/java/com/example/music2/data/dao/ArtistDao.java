package com.example.music2.data.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.music2.database.Artist;

import java.util.List;

@Dao
public interface ArtistDao {

    @Insert
    long insert(Artist artist);

    @Update
    void update(Artist artist);

    @Delete
    void delete(Artist artist);

    @Query("SELECT * FROM artists ORDER BY name ASC")
    List<Artist> getAllArtists();

    @Query("SELECT * FROM artists WHERE id = :artistId LIMIT 1")
    Artist getArtistById(int artistId);
}
