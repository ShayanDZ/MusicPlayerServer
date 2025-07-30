package com.hertz.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {

    // Immutable properties
    private final int id;
    private final String username;
    private final LocalDateTime registrationDate;

    // Mutable properties
    private String email;
    private String hashedPassword;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String fullName;
    private String profileImageUrl;
    private final List<Integer> tracks = new ArrayList<>();
    private final List<Integer> likedSongs = new ArrayList<>();
    private final Playlist recentlyPlayed;
    private final List<Playlist> playlists = new ArrayList<>();

    public User(String username, String email, String fullName, String hashedPassword, LocalDateTime registrationDate, Integer id) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.id = (id == null || id == 0) ? (generateId(username, email, registrationDate)) : id;
        recentlyPlayed = new Playlist("Recently Played", this.id, "Tracks played recently",new LinkedList<Integer>());
    }

    public List<Integer> getTracks() {
        return tracks;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private static int generateId(String username, String email, LocalDateTime registrationDate) {
        return (username + email + registrationDate.toString() + System.currentTimeMillis() + (int) (Math.random() * 1000)).hashCode();
    }

    public List<Integer> getLikedSongs() {
        return likedSongs;
    }

    public Playlist getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
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
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", fullName='" + fullName + '\'' +
                ", profileImageUrl='" + profileImageUrl;
    }
    public boolean addTrack(int trackId) {
        if (!tracks.contains(trackId)) {
            tracks.add(trackId);
            return true;
        }
        return false;
    }
    public boolean removeTrack(int trackId) {
        return tracks.remove(Integer.valueOf(trackId));
    }
}
