package com.hertz.repository;

import com.hertz.model.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicRepository {
    private List<Music> musicList = new ArrayList<>();

    public MusicRepository() {
        // Load music data from a file or initialize with dummy data
        loadMusicData();
    }

    private void loadMusicData() {
        // Implement logic to load music data from a file or a database
        //musicList.add(new Music("Song 1", "Artist 1", "Genre 1"));
        //musicList.add(new Music("Song 2", "Artist 2", "Genre 2"));
        // Add more songs as needed
    }

    public List<Music> getAllMusic() {
        return musicList;
    }
}