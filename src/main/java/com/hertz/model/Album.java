package com.hertz.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Album {

    // Immutable properties
    private final String id;
    private final String title;
    private final Artist artist;
    private final LocalDate releaseDate;
    private final String genre;
    private final String description;

    // Mutable properties
    private String coverImageUrl = null;

    public Album(String title, Artist artist, LocalDate releaseDate,
                 String coverImageUrl, String genre, String description) {
        this.title = title;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.coverImageUrl = coverImageUrl;
        this.genre = genre;
        this.description = description;
        this.id = generateId(title, artist);
    }

    private String generateId(String title, Artist artist) {
        // TODO: implement a complex ID generation logic for albums
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album)) {
            return false;
        }
        Album album = (Album) o;
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Album: %s by %s", title, artist.toString());
    }
}

