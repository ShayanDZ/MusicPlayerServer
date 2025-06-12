package com.hertz.repository;


import com.hertz.model.Response;
import com.hertz.model.User;
import com.hertz.utils.DatabaseConnection;
import com.mongodb.Block;
import org.bson.Document;

import java.time.LocalDate;
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
                    LocalDate registrationDate = userDocument.getDate("registrationDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int id = userDocument.getInteger("id");
                    ArrayList likedSongs = (ArrayList) userDocument.get("likedSongs");
                    ArrayList recentlyPlayed = (ArrayList) userDocument.get("recentlyPlayed");
                    ArrayList playlists = (ArrayList) userDocument.get("playlists");
                    User user = new User(username, email, fullName, hashedPassword, registrationDate,id);
                    user.getLikedSongs().addAll(likedSongs);
                    user.getRecentlyPlayed().addAll(recentlyPlayed);
                    user.getPlaylists().addAll(playlists);
                    userList.add(user);
                });
    }
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    public Response addUser(User user) {
        if (userList.contains(user)) {
            return Response.USER_ALREADY_EXIST;
        }
        userList.add(user);
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Document userDocument = new Document("username", user.getUsername())
                .append("email", user.getEmail())
                .append("fullName", user.getFullName())
                .append("hashedPassword", user.getPassword())
                .append("registrationDate", java.util.Date.from(user.getRegistrationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("id", user.getId())
                .append("likedSongs", user.getLikedSongs())
                .append("recentlyPlayed", user.getRecentlyPlayed())
                .append("playlists", user.getPlaylists());
        databaseConnection.getDatabase().getCollection("users").insertOne(userDocument);
        return Response.signUpSuccess;
    }
    public List<User> getAllUser() {
        return userList;
    }
}
