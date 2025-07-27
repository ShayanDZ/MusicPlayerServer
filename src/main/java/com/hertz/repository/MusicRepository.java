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
                    Artist artist = (Artist)musicDocument.get("artist");
                    String genre = musicDocument.getString("genre");
                    int durationInSeconds = musicDocument.getInteger("durationInSeconds");
                    LocalDateTime releaseDate = musicDocument.getDate("releaseDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    Album album = (Album)musicDocument.get("album");
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
}