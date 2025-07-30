package com.hertz.repository;


import com.hertz.model.Playlist;
import com.hertz.model.Response;
import com.hertz.model.User;
import com.hertz.utils.DatabaseConnection;
import com.mongodb.Block;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private List<User> userList = new ArrayList<>();

    private UserRepository() {
        loadUserData();
    }

    private void loadUserData() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        databaseConnection.getDatabase()
                .getCollection("users")
                .find()
                .forEach((Block<? super Document>) (Document userDocument) -> {
                    String username = userDocument.getString("username");
                    String email = userDocument.getString("email");
                    String fullName = userDocument.getString("fullName");
                    String hashedPassword = userDocument.getString("hashedPassword");
                    LocalDateTime registrationDate = userDocument.getDate("registrationDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    int id = userDocument.getInteger("id");
                    ArrayList tracks = (ArrayList) userDocument.get("tracks");
                    ArrayList likedSongs = (ArrayList) userDocument.get("likedSongs");
                    Playlist recentlyPlayed = Playlist.fromDocument((Document) userDocument.get("recentlyPlayed"));
                    ArrayList playlists = (ArrayList) userDocument.get("playlists");
                    User user = new User(username, email, fullName, hashedPassword, registrationDate,id);
                    if (tracks != null) {
                        user.getTracks().addAll(tracks);
                    }
                    if (likedSongs != null) {
                        user.getLikedSongs().addAll(likedSongs);
                    }
                    if (recentlyPlayed.getTracks() != null) {
                        user.getRecentlyPlayed().getTracks().addAll(recentlyPlayed.getTracks());
                    }
                    if (playlists != null) {
                        user.getPlaylists().addAll(playlists);
                    }
                    userList.add(user);
                });
    }
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    public synchronized Response addUser(User user) {
        if (userList.contains(user)) {
            return Response.USER_ALREADY_EXIST;
        }
        userList.add(user);
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Document userDocument = new Document("username", user.getUsername())
                .append("email", user.getEmail())
                .append("fullName", user.getFullName())
                .append("hashedPassword", user.getHashedPassword())
                .append("registrationDate", java.util.Date.from(user.getRegistrationDate().atZone(ZoneId.systemDefault()).toInstant()))
                .append("id", user.getId())
                .append("tracks", user.getTracks())
                .append("likedSongs", user.getLikedSongs())
                .append("recentlyPlayed", user.getRecentlyPlayed().convertToDocument())
                .append("playlists", user.getPlaylists());
        databaseConnection.getDatabase().getCollection("users").insertOne(userDocument);
        return Response.signUpSuccess;
    }
    public synchronized List<User> getAllUser() {
        return userList;
    }

    public synchronized boolean updateUser(User user) {
        try {
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            Document updatedUserDocument = new Document("username", user.getUsername())
                    .append("email", user.getEmail())
                    .append("fullName", user.getFullName())
                    .append("hashedPassword", user.getHashedPassword())
                    .append("registrationDate", java.util.Date.from(user.getRegistrationDate().atZone(ZoneId.systemDefault()).toInstant()))
                    .append("id", user.getId())
                    .append("tracks", user.getTracks())
                    .append("likedSongs", user.getLikedSongs())
                    .append("recentlyPlayed", user.getRecentlyPlayed().convertToDocument())
                    .append("playlists", user.getPlaylists().stream()
                            .map(Playlist::convertToDocument)
                            .toList());

            databaseConnection.getDatabase().getCollection("users")
                    .updateOne(new Document("id", user.getId()), new Document("$set", updatedUserDocument));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public synchronized User findByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public synchronized User findByEmail(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
