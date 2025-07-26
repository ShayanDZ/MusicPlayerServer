package com.hertz.model;

import java.time.LocalDate;
import java.util.Objects;

public class Music {

    // Immutable properties
    private final int id;
    private final String title;
    private final Artist artist;
    private final String genre;
    private final int durationInSeconds;
    private final LocalDate releaseDate;
    private final LocalDate addedDate;
    private final Album album;
    private final String extension;

    // Mutable properties
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    private int likeCount;
    private boolean isLiked = false;

    public Music(String title, Artist artist, String genre, int durationInSeconds, LocalDate releaseDate, Album album, Integer id, String extension, String base64) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.durationInSeconds = durationInSeconds;
        this.releaseDate = releaseDate;
        this.extension = extension;
        this.addedDate = LocalDate.now();
        this.album = album;
        this.base64 = base64;
        this.id = ((id==null || id==0)?(generateId(title, artist, releaseDate)):id);
    }

    private static int generateId(String title, Artist artist, LocalDate releaseDate) {
        return (title + artist.getName() + releaseDate.toString()).hashCode();
    }


    public Artist getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public Album getAlbum() {
        return album;
    }

    public String getExtension() {
        return extension;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
