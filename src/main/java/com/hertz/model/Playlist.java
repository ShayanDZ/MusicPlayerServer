package com.hertz.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {

    // Immutable properties
    private final String id;
    private final Music owner;
    private final LocalDate createdDate;

    // Mutable properties
    private String name;
    private String description;
    private final List<Music> tracks;

    public Playlist(String name, Music owner, String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.createdDate = LocalDate.now();
        this.id = generateId(name, owner);
        this.tracks = new ArrayList<>();
    }

    private String generateId(String name, Music owner) {
        // TODO: implement custom playlist ID logic (e.g., username + name + date)
        return "";
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
