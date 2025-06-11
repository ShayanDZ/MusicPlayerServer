package com.hertz.repository;


import com.hertz.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> userList = new ArrayList<>();

    public UserRepository() {
        // Load music data from a file or initialize with dummy data
        loadUserData();
    }

    private void loadUserData() {
        // Implement logic to load music data from a file or a database
        //musicList.add(new Music("Song 1", "Artist 1", "Genre 1"));
        //musicList.add(new Music("Song 2", "Artist 2", "Genre 2"));
        // Add more songs as needed
    }

    public List<User> getAllMusic() {
        return userList;
    }
}
