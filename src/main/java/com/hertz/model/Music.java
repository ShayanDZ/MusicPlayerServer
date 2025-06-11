package com.hertz.model;

import java.time.LocalDate;
import java.util.Objects;

public class Music {

    // Immutable properties
    private final String id;
    private final String title;
    private final Artist artist;
    private final Genre genre;
    private final int durationInSeconds;
    private final LocalDate releaseDate;
    private final LocalDate addedDate;
    private final Album album;

    // Mutable properties
    private int likeCount;
    private boolean isLiked;

    public Music(String title, Artist artist, Genre genre, int durationInSeconds, LocalDate releaseDate, Album album) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.durationInSeconds = durationInSeconds;
        this.releaseDate = releaseDate;
        this.addedDate = LocalDate.now();
        this.album = album;
        this.id = generateId(title, artist, releaseDate);
        this.isLiked = false;
    }

    private String generateId(String title, Artist artist, LocalDate releaseDate) {
        // TODO: implement a complex ID generation logic
        return "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Music)) return false;
        Music music = (Music) o;
        return Objects.equals(id, music.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Track: %s by %s", title, artist.toString());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
