package com.hertz.model;

import com.hertz.repository.UserRepository;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Playlist {

    // Immutable properties
    private final int id;

    private final int ownerId;
    private final LocalDateTime createdDate;
    private final List<Integer> tracks;

    // Mutable properties
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public int getId() {
        return id;
    }

    public int getOwnerID() {
        return ownerId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Integer> getTracks() {
        return tracks;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Playlist(String name, int ownerId, String description) {
        this.name = name;
        this.ownerId = ownerId;
        this.description = description;
        this.createdDate = LocalDateTime.now();
        this.id = generateId(name, ownerId, createdDate);
        this.tracks = new ArrayList<>();
    }

    public Playlist(String name, int ownerId, String description, List<Integer> tracks) {
        this.name = name;
        this.ownerId = ownerId;
        this.description = description;
        this.createdDate = LocalDateTime.now();
        this.id = generateId(name, ownerId, createdDate);
        this.tracks = tracks;
    }
    public Playlist(String name, int ownerId,LocalDateTime localDateTime, String description, List<Integer> tracks) {
        this.name = name;
        this.ownerId = ownerId;
        this.description = description;
        this.createdDate = localDateTime;
        this.id = generateId(name, ownerId, createdDate);
        this.tracks = tracks;
    }

    private static int generateId(String name, int ownerId, LocalDateTime createdDate) {
        return (name + ownerId + createdDate.toString()).hashCode();
    }

    public boolean addTrack(int trackId) {
        if (getName().equals("Recently Played")) {
            LinkedList<Integer> tracks = (LinkedList<Integer>) this.tracks; // Use LinkedList for Recently Played to maintain order
            if (tracks.contains(trackId)) {
                tracks.remove(trackId); // Remove the track if it already exists to move it to the front
            }
            tracks.addFirst(trackId); // Add to the front for recently played
            if (tracks.size() > 200) { // Limit to 200 tracks
                tracks.removeLast(); // Remove the oldest track
            }
            return true;
        } else {
            if (!tracks.contains(trackId)) {
                tracks.add(trackId);
                return true;
            }
            return false;
        }
    }

    public boolean removeTrack(int trackId) {
        return tracks.remove((Integer) trackId);
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
        return "Playlist{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", createdDate=" + createdDate +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Document convertToDocument() {
        Document playlistDocument = new Document();
        playlistDocument.append("id", this.id);
        playlistDocument.append("name", this.name);
        playlistDocument.append("description", this.description);
        playlistDocument.append("createdDate", java.util.Date.from(this.createdDate.atZone(ZoneId.systemDefault()).toInstant()));
        playlistDocument.append("ownerId", this.ownerId); // Store owner ID instead of the full object
        playlistDocument.append("tracks", this.tracks);
        return playlistDocument;
    }

    public static Playlist fromDocument(Document document) {
        int id = document.getInteger("id");
        String name = document.getString("name");
        String description = document.getString("description");
        LocalDateTime createdDate = document.getDate("createdDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        int ownerId = document.getInteger("ownerId");
        List<Integer> tracks = (List<Integer>) document.get("tracks");

        return new Playlist(name, ownerId,createdDate, description, tracks);
    }
}
