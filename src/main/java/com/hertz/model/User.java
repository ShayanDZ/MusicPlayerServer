package com.hertz.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    // Immutable properties
    private final String id;
    private final String username;
    private final String email;
    private final LocalDate registrationDate;

    // Mutable properties
    private String password;
    private String fullName;
    private String profileImageUrl;
    private final List<Music> likedSongs;
    private final List<Music> recentlyPlayed;
    private final List<Playlist> playlists;

    public User(String username, String email, String fullName, String password, LocalDate registrationDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.id = generateId(username, email);
        this.likedSongs = new ArrayList<>();
        this.recentlyPlayed = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private String generateId(String username, String email) {
        // TODO: implement a ID logic
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("User: %s", username);
    }
}
