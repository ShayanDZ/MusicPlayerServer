package com.hertz.repository;

import com.hertz.model.*;
import com.hertz.utils.DatabaseConnection;
import com.mongodb.Block;
import org.bson.Document;

import java.time.LocalDate;
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
                    Genre genre = (Genre) musicDocument.get("genre");
                    int durationInSeconds = musicDocument.getInteger("durationInSeconds");
                    LocalDate releaseDate = musicDocument.getDate("releaseDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    Album album = (Album)musicDocument.get("album");
                    int id = musicDocument.getInteger("id");
                    Music music = new Music(title, artist, genre, durationInSeconds, releaseDate, album, id);
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