package com.hertz.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Album {

    // Immutable properties
    private final int id;
    private final String title;
    private final Artist artist;

    // Mutable properties

    public Album(String title, Artist artist,Integer id) {
        this.title = title;
        this.artist = artist;
        this.id = (id == null || id == 0) ? (generateId(title, artist)) : id;
    }

    private int generateId(String title, Artist artist) {
        return (title+artist.getName() + System.currentTimeMillis() + (int) (Math.random() * 1000)).hashCode(); // Simple ID generation logic
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

