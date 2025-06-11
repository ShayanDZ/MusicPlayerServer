package com.hertz.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Artist {

    // Immutable properties
    private final String id;
    private final String name;
    private final String bio;
    private final String profileImageUrl;
    private final List<Genre> genres;

    // Mutable properties
    private final List<Music> songs;
    private final List<Album> albums;

    public Artist(String name, String bio, String profileImageUrl, List<Genre> genres) {
        this.name = name;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.genres = genres != null ? new ArrayList<>(genres) : new ArrayList<>();
        this.id = generateId(name);
        this.songs = new ArrayList<>();
        this.albums = new ArrayList<>();
    }

    private String generateId(String name) {
        // TODO: implement a complex ID generation logic
        return "";
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Artist: %s", name);
    }
}
