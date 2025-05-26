package com.example.music2.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artists")
public class Artist {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    // Constructor
    public Artist(String name) {
        this.name = name;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
