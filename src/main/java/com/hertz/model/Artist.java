package com.hertz.model;

import org.bson.Document;

import java.util.Objects;

public class Artist {

    // Immutable properties
    private final int id;
    private final String name;

    // Default constructor for serialization frameworks
    public Artist() {
        this.id = 0;
        this.name = "";
    }

    // Constructor with parameters
    public Artist(String name, Integer id) {
        this.name = name;
        this.id = (id == null || id == 0) ? generateId(name) : id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private int generateId(String name) {
        return (name + System.currentTimeMillis() + (int) (Math.random() * 1000)).hashCode();
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
    public Document convertToDocument() {
        Document artistDocument = new Document();
        artistDocument.append("id", this.getId());
        artistDocument.append("name", this.getName());
        return artistDocument;
    }
    public static Artist fromDocument(Document document) {
        int id = document.getInteger("id");
        String name = document.getString("name");
        return new Artist(name, id);
    }
}