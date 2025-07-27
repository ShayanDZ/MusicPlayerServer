package com.hertz.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Artist {

    // Immutable properties
    private final int id;
    private final String name;

    // Mutable properties

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Artist(String name,Integer id) {
        this.name = name;
        this.id = (id == null || id == 0) ? (generateId(name)) : id;
    }

    private int generateId(String name) {
        return (name+ System.currentTimeMillis() + (int) (Math.random() * 1000)).hashCode(); // Simple ID generation logic
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
