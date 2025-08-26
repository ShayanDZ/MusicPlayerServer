package com.hertz.model;

import org.bson.Document;

import java.util.Objects;

public class Album {

    // Immutable properties
    private final int id;
    private final String title;
    private final Artist artist;

    // Mutable properties

    public Album(String title, Artist artist, Integer id) {
        this.title = title;
        this.artist = artist;
        this.id = (id == null || id == 0) ? (generateId(title, artist)) : id;
    }

    private int generateId(String title, Artist artist) {
        return (title + artist.getName() + System.currentTimeMillis() + (int) (Math.random() * 1000)).hashCode();
    }

    public String getTitle() {
        return title;
    }

    public Artist getArtist() {
        return artist;
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

    public Document convertToDocument() {
        Document albumDocument = new Document();
        albumDocument.append("id", this.id);
        albumDocument.append("title", this.title);
        albumDocument.append("artist", this.artist.convertToDocument());
        return albumDocument;
    }
    public static Album fromDocument(Document document) {
        int id = document.getInteger("id");
        String title = document.getString("title");
        Document artistDocument = (Document) document.get("artist");
        Artist artist = Artist.fromDocument(artistDocument);
        return new Album(title, artist, id);
    }

}

