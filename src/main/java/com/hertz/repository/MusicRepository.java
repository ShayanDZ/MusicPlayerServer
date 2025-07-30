package com.hertz.repository;

import com.hertz.model.*;
import com.hertz.utils.DatabaseConnection;
import com.mongodb.Block;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MusicRepository {
    private static MusicRepository instance;
    private List<Music> musicList = new ArrayList<>();


    private MusicRepository() {
        loadMusicData();
    }
    private void loadMusicData() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        databaseConnection.getDatabase()
                .getCollection("musics")
                .find()
                .forEach((Block<? super Document>) (Document musicDocument) -> {
                    String title = musicDocument.getString("title");
                    Document artistDocument = (Document) musicDocument.get("artist");
                    Artist artist = Artist.fromDocument(artistDocument);
                    String genre = musicDocument.getString("genre");
                    int durationInSeconds = musicDocument.getInteger("durationInSeconds");
                    LocalDateTime releaseDate = musicDocument.getDate("releaseDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    Document albumDocument = (Document) musicDocument.get("album");
                    Album album = Album.fromDocument(albumDocument);
                    int id = musicDocument.getInteger("id");
                    String extension = musicDocument.getString("extension");
                    String base64 = musicDocument.getString("base64");
                    Music music = new Music(title, artist, genre, durationInSeconds, releaseDate, album, id, extension,base64);
                    musicList.add(music);
                });

    }
    public static MusicRepository getInstance() {
        if (instance == null) {
            instance = new MusicRepository();
        }
        return instance;
    }
    public List<Music> getAllMusic() {
        return musicList;
    }
    public Response addMusic(Music music) {
        if (musicList.contains(music)) {
            return Response.musicAlreadyExists;
        }
        musicList.add(music);
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Document musicDocument = new Document("id", music.getId())
                .append("title", music.getTitle())
                .append("artist", music.getArtist().convertToDocument())
                .append("genre", music.getGenre())
                .append("durationInSeconds", music.getDurationInSeconds())
                .append("releaseDate", java.util.Date.from(music.getReleaseDate().atZone(ZoneId.systemDefault()).toInstant()))
                .append("addedDate", java.util.Date.from(music.getAddedDate().atZone(ZoneId.systemDefault()).toInstant()))
                .append("album", music.getAlbum().convertToDocument())
                .append("extension", music.getExtension())
                .append("base64", music.getBase64())
                .append("likeCount", music.getLikeCount())
                .append("isLiked", music.isLiked());
        databaseConnection.getDatabase().getCollection("musics").insertOne(musicDocument);
        return Response.uploadMusicSuccess;
    }
    public boolean updateMusic(Music music) {
        try {
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            Document updatedMusicDocument = new Document("id", music.getId())
                    .append("title", music.getTitle())
                    .append("artist", music.getArtist().convertToDocument())
                    .append("genre", music.getGenre())
                    .append("durationInSeconds", music.getDurationInSeconds())
                    .append("releaseDate", java.util.Date.from(music.getReleaseDate().atZone(ZoneId.systemDefault()).toInstant()))
                    .append("addedDate", java.util.Date.from(music.getAddedDate().atZone(ZoneId.systemDefault()).toInstant()))
                    .append("album", music.getAlbum().convertToDocument())
                    .append("extension", music.getExtension())
                    .append("base64", music.getBase64())
                    .append("likeCount", music.getLikeCount())
                    .append("isLiked", music.isLiked());

            databaseConnection.getDatabase().getCollection("musics")
                    .updateOne(new Document("id", music.getId()), new Document("$set", updatedMusicDocument));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Music findMusicById(int id) {
        for (Music music : musicList) {
            if (music.getId() == id) {
                return music;
            }
        }
        return null; // Return null if no music with the given ID is found
    }
}