package com.example.m.data.dao;

import androidx.room.*;
import com.example.m.data.entities.Album;
import java.util.List;

@Dao
public interface AlbumDao {
    @Insert
    long insert(Album album);

    @Update
    void update(Album album);

    @Delete
    void delete(Album album);

    @Query("SELECT * FROM albums WHERE artistId = :artistId")
    List<Album> findByArtistId(int artistId);

    @Query("SELECT * FROM albums")
    List<Album> getAllAlbums();
}
