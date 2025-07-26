package com.hertz.model;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hertz.utils.PasswordUtils.hashPassword;

public class User {

    // Immutable properties
    private final int id;
    private final String username;
    private final String email;
    private final LocalDate registrationDate;

    // Mutable properties
    private String hashedPassword;
    private String fullName;
    private String profileImageUrl;
    private final List<Music> tracks = new ArrayList<>();
    private final List<Music> likedSongs = new ArrayList<>();
    private final List<Music> recentlyPlayed = new ArrayList<>();
    private final List<Playlist> playlists = new ArrayList<>();

    public User(String username, String email, String fullName, String hashedPassword, LocalDate registrationDate, Integer id) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.id = (id == null || id == 0) ? (generateId(username, email)) : id;
    }

    public List<Music> getTracks() {
        return tracks;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private static int generateId(String username, String email) {
        return (username + email).hashCode();
    }

    public List<Music> getLikedSongs() {
        return likedSongs;
    }

    public List<Music> getRecentlyPlayed() {
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

    public LocalDate getRegistrationDate() {
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
}
