package com.hertz.model;

import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class Music {

    // Immutable properties
    private final int id;
    private final String title;
    private final Artist artist;
    private final String genre;
    private final int durationInSeconds;
    private final LocalDateTime releaseDate;
    private final LocalDateTime addedDate;
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
    private boolean isPublic = false;

    public Music(String title, Artist artist, String genre, int durationInSeconds, LocalDateTime releaseDate, Album album, Integer id, String extension, String base64) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.durationInSeconds = durationInSeconds;
        this.releaseDate = releaseDate;
        this.extension = extension;
        this.addedDate = LocalDateTime.now();
        this.album = album;
        this.base64 = base64;
        this.id = ((id==null || id==0)?(generateId(title, artist, releaseDate)):id);
    }
    public Music(String title, Artist artist, String genre, int durationInSeconds, LocalDateTime releaseDate,LocalDateTime addedDate, Album album, Integer id, String extension, String base64) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.durationInSeconds = durationInSeconds;
        this.releaseDate = releaseDate;
        this.extension = extension;
        this.addedDate = addedDate;
        this.album = album;
        this.base64 = base64;
        this.id = ((id==null || id==0)?(generateId(title, artist, releaseDate)):id);
    }

    private static int generateId(String title, Artist artist, LocalDateTime releaseDate) {
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

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getAddedDate() {
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

    public boolean isPublic() {
        return isPublic;
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
    public Document convertToDocument() {
        Document musicDocument = new Document();
        musicDocument.append("id", this.id);
        musicDocument.append("title", this.title);
        musicDocument.append("artist", this.artist.convertToDocument());
        musicDocument.append("genre", this.genre);
        musicDocument.append("durationInSeconds", this.durationInSeconds);
        musicDocument.append("releaseDate", java.util.Date.from(this.releaseDate.atZone(ZoneId.systemDefault()).toInstant()));
        musicDocument.append("addedDate", java.util.Date.from(this.addedDate.atZone(ZoneId.systemDefault()).toInstant()));
        musicDocument.append("album", this.album.convertToDocument());
        musicDocument.append("extension", this.extension);
        musicDocument.append("base64", this.base64);
        musicDocument.append("likeCount", this.likeCount);
        musicDocument.append("isLiked", this.isPublic);
        return musicDocument;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public static Music fromDocument(Document document) {
        int id = document.getInteger("id");
        String title = document.getString("title");
        Document artistDocument = (Document) document.get("artist");
        Artist artist = Artist.fromDocument(artistDocument);
        String genre = document.getString("genre");
        int durationInSeconds = document.getInteger("durationInSeconds");
        LocalDateTime releaseDate = document.getDate("releaseDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime addedDate = document.getDate("addedDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Document albumDocument = (Document) document.get("album");
        Album album = Album.fromDocument(albumDocument);
        String extension = document.getString("extension");
        String base64 = document.getString("base64");
        int likeCount = document.getInteger("likeCount");
        boolean isLiked = document.getBoolean("isLiked");

        Music music = new Music(title, artist, genre, durationInSeconds, releaseDate,addedDate, album, id, extension, base64);
        music.setLikeCount(likeCount);
        music.isPublic = isLiked;
        return music;
    }
}
