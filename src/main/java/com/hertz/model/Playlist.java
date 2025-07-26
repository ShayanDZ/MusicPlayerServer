package com.hertz.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {

    // Immutable properties
    private final int id;
    private final User owner;
    private final LocalDate createdDate;

    // Mutable properties
    private String name;
    private String description;
    private final List<Music> tracks;

    public Playlist(String name, User owner, String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.createdDate = LocalDate.now();
        this.id = generateId(name, owner,createdDate);
        this.tracks = new ArrayList<>();
    }

    private static int generateId(String name, User owner, LocalDate createdDate) {
        return (name + owner.getUsername() + createdDate.toString()).hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Playlist)) {
            return false;
        }
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Playlist: %s (%s) by %s", name, owner.toString());
    }
}
