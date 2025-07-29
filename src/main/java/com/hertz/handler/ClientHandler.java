package com.hertz.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hertz.model.*;
import com.hertz.repository.MusicRepository;
import com.hertz.repository.UserRepository;
import com.hertz.utils.DatabaseConnection;
import com.hertz.utils.DateParser;
import com.hertz.utils.PasswordUtils;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.hertz.utils.PasswordUtils.verifyPassword;

public class ClientHandler extends Thread {
    private static final Gson gson = new Gson();
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            StringBuilder requestBuilder = new StringBuilder();
            java.lang.String inputLine;

            // Read the request from the client
            while ((inputLine = in.readLine()) != null) {
                requestBuilder.append(inputLine);
                if (inputLine.isEmpty()) {
                    break;
                }
            }

            java.lang.String requestString = requestBuilder.toString();
            System.out.println("Request received: " + requestString);

            // Parse the JSON request using GSON
            JsonObject requestJson = gson.fromJson(requestString, JsonObject.class);
            java.lang.String action = requestJson.get("Request").getAsString();

            JsonObject responseJson;

            switch (action) {
                case "signUp":
                    responseJson = handleSignUp(requestJson.getAsJsonObject("Payload"));
                    break;
                case "logIn":
                    responseJson = handleLogIn(requestJson.getAsJsonObject("Payload"));
                    break;
                case "uploadMusic":
                    responseJson = handleUploadMusic(requestJson.getAsJsonObject("Payload"));
                    break;
                case "getUserMusicList":
                    responseJson = handleGetUserMusicList(requestJson.getAsJsonObject("Payload"));
                    break;
                case "deleteMusic":
                    responseJson = handleDeleteMusic(requestJson.getAsJsonObject("Payload"));
                    break;
                case "downloadMusic":
                    responseJson = handleDownloadMusic(requestJson.getAsJsonObject("Payload"));
                    break;
                case "likeSong":
                    responseJson = handleLikeSong(requestJson.getAsJsonObject("Payload"));
                    break;
                case "dislikeSong":
                    responseJson = handleDislikeSong(requestJson.getAsJsonObject("Payload"));
                    break;
                case "getUserPlaylists":
                    responseJson = handleGetUserPlaylists(requestJson.getAsJsonObject("Payload"));
                    break;
                default:
                    responseJson = new JsonObject();
                    responseJson.addProperty("status", Response.InvalidRequest.toString());
                    responseJson.addProperty("message", "Unknown request type");
                    break;
            }

            // Send response back to the client
            out.println(responseJson.toString());
        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private JsonObject handleSignUp(JsonObject payload) {
        JsonObject response = new JsonObject();
        java.lang.String fullname = payload.get("fullname").getAsString();
        java.lang.String username = payload.get("username").getAsString();
        java.lang.String email = payload.get("email").getAsString();
        java.lang.String password = payload.get("password").getAsString();
        UserRepository userRepository = UserRepository.getInstance();

        boolean emailExist = userRepository.getAllUser().stream()
                .anyMatch(user -> user.getEmail().equals(email));
        boolean usernameExist = userRepository.getAllUser().stream()
                .anyMatch(user -> user.getUsername().equals(username));

        if (emailExist) {
            response.addProperty("status", Response.emailAlreadyExist.toString());
            response.addProperty("message", "Email already exists");
            return response;
        }
        if (usernameExist) {
            response.addProperty("status", Response.usernameAlreadyExist.toString());
            response.addProperty("message", "Username already exists");
            return response;
        }

        java.lang.String hashedPassword = PasswordUtils.hashPassword(password);
        User user = new User(username, email, fullname, hashedPassword, LocalDateTime.now(), 0);
        Response responseMessage = userRepository.addUser(user);
        response.addProperty("status", responseMessage.toString());
        response.addProperty("message", responseMessage.toString());
        return response;
    }

    private JsonObject handleLogIn(JsonObject payload) {
        java.lang.String username = payload.get("username").getAsString();
        java.lang.String password = payload.get("password").getAsString();

        System.out.println("Username received: " + username);
        System.out.println("Password received: " + password);

        User temp = UserRepository.getInstance().getAllUser().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny()
                .orElse(null);

        if (temp != null) {
            System.out.println("User found: " + temp.getUsername());
        } else {
            System.out.println("User not found in repository.");
        }

        JsonObject response = new JsonObject();
        if (temp != null) {
            if (verifyPassword(temp.getHashedPassword(), password)) {
                response.addProperty("status", Response.logInSuccess.toString());
                response.addProperty("message", "User logged in successfully");
                response.addProperty("username", temp.getUsername());
                response.addProperty("email", temp.getEmail());
                response.addProperty("fullname", temp.getFullName());
                response.addProperty("registrationDate", temp.getRegistrationDate().toString());
                response.addProperty("profileImageUrl", temp.getProfileImageUrl() != null ? temp.getProfileImageUrl() : "");
            } else {
                response.addProperty("status", Response.incorrectPassword.toString());
                response.addProperty("message", "Password is Incorrect");
            }
        } else {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "Invalid username");
        }
        return response;
    }

    private JsonObject handleUploadMusic(JsonObject payload) {
        JsonObject response = new JsonObject();
        String username = payload.get("username").getAsString();
        JsonObject musicMap = payload.getAsJsonObject("musicMap");
        String base64Data = payload.get("base64Data").getAsString();

        UserRepository userRepository = UserRepository.getInstance();
        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "User not found");
            return response;
        }

        try {
            // Parse artist from musicMap
            JsonObject artistMap = musicMap.getAsJsonObject("artist");
            String artistName = artistMap.get("name").getAsString();
            Integer artist_id = artistMap.has("id") ? artistMap.get("id").getAsInt() : 0;

            Artist artist = new Artist(artistName, artist_id);

            // Parse album from musicMap
            JsonObject albumMap = musicMap.getAsJsonObject("album");
            String albumTitle = albumMap.get("title").getAsString();
            Integer album_id = albumMap.has("id") ? albumMap.get("id").getAsInt() : 0;
            Album album = new Album(albumTitle, artist, album_id);

            // Parse music from musicMap
            String title = musicMap.get("title").getAsString();
            String genre = musicMap.get("genre").getAsString();
            int durationInSeconds = musicMap.get("durationInSeconds").getAsInt();
            LocalDateTime releaseDate = DateParser.parseIso8601Date(musicMap.get("releaseDate").getAsString());
            int id = musicMap.get("id").getAsInt();
            String extension = musicMap.get("extension").getAsString();
            Music music = new Music(title, artist, genre, durationInSeconds, releaseDate, album, id, extension, base64Data);
            // Add music to user's tracks
            user.addTrack(id);
            userRepository.updateUser(user);
            // Save music to database
            Response responseMessage = MusicRepository.getInstance().addMusic(music);

            response.addProperty("status", responseMessage.toString());
            response.addProperty("message", responseMessage.toString());
        } catch (Exception e) {
            response.addProperty("status", Response.uploadMusicFailed.toString());
            response.addProperty("message", "Failed to upload music: " + e.getMessage());
        }

        return response;
    }

    private JsonObject handleGetUserMusicList(JsonObject payload) {
        JsonObject response = new JsonObject();
        String username = payload.get("username").getAsString();

        UserRepository userRepository = UserRepository.getInstance();
        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "User not found");
            return response;
        }

        try {
            List<Integer> userMusicListIDs = user.getTracks();
            List<JsonObject> musicJsonList = new ArrayList<>();
            MusicRepository musicRepository = MusicRepository.getInstance();
            List<Music> userMusicList = musicRepository.getAllMusic().stream()
                    .filter(music -> userMusicListIDs.contains(music.getId()))
                    .toList();
            for (Music music : userMusicList) {
                JsonObject musicJson = new JsonObject();
                musicJson.addProperty("id", music.getId());
                musicJson.addProperty("title", music.getTitle());
                musicJson.addProperty("artist", music.getArtist().getName());
                musicJson.addProperty("genre", music.getGenre());
                musicJson.addProperty("durationInSeconds", music.getDurationInSeconds());
                musicJson.addProperty("releaseDate", music.getReleaseDate().toString());
                musicJson.addProperty("extension", music.getExtension());
                musicJsonList.add(musicJson);
            }

            response.addProperty("status", Response.getUserMusicListSuccess.toString());
            response.add("Payload", gson.toJsonTree(musicJsonList));
        } catch (Exception e) {
            response.addProperty("status", Response.getUserMusicListFailed.toString());
            response.addProperty("message", "Failed to retrieve music list: " + e.getMessage());
        }

        return response;
    }

    private JsonObject handleDeleteMusic(JsonObject payload) {
        JsonObject response = new JsonObject();
        String username = payload.get("username").getAsString();
        int musicId = payload.get("musicId").getAsInt();

        UserRepository userRepository = UserRepository.getInstance();
        MusicRepository musicRepository = MusicRepository.getInstance();

        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "User not found");
            return response;
        }

        Music music = musicRepository.getAllMusic().stream()
                .filter(m -> m.getId() == musicId)
                .findFirst()
                .orElse(null);

        if (music == null) {
            response.addProperty("status", Response.musicNotFound.toString());
            response.addProperty("message", "Music not found");
            return response;
        }

        try {
            // Remove music from user's tracks
            boolean removed = user.removeTrack(musicId);

            if (removed) {
                response.addProperty("status", Response.deleteMusicSuccess.toString());
                response.addProperty("message", "Music removed from user's tracks successfully");
            } else {
                response.addProperty("status", Response.musicNotFoundForUser.toString());
                response.addProperty("message", "Music not found in user's tracks");
            }
        } catch (Exception e) {
            response.addProperty("status", Response.deleteMusicFailed.toString());
            response.addProperty("message", "Failed to remove music: " + e.getMessage());
        }

        return response;
    }

    private JsonObject handleDownloadMusic(JsonObject payload) {
        JsonObject response = new JsonObject();
        String username = payload.get("username").getAsString();
        int musicId = payload.get("musicId").getAsInt();

        UserRepository userRepository = UserRepository.getInstance();
        MusicRepository musicRepository = MusicRepository.getInstance();

        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "User not found");
            return response;
        }

        Music music = musicRepository.getAllMusic().stream()
                .filter(m -> m.getId() == musicId)
                .findFirst()
                .orElse(null);

        if (music == null) {
            response.addProperty("status", Response.musicNotFound.toString());
            response.addProperty("message", "Music not found");
            return response;
        }

        try {
            String base64Data = music.getBase64();
            if (base64Data != null && !base64Data.isEmpty()) {
                response.addProperty("status", Response.downloadMusicSuccess.toString());
                response.addProperty("Payload", base64Data);
            } else {
                response.addProperty("status", Response.dataNotFound.toString());
                response.addProperty("message", "Base64 data not available for the requested music");
            }
        } catch (Exception e) {
            response.addProperty("status", Response.downloadMusicFailed.toString());
            response.addProperty("message", "Failed to retrieve music: " + e.getMessage());
        }

        return response;
    }

    private JsonObject handleLikeSong(JsonObject payload) {
        JsonObject response = new JsonObject();
        String username = payload.get("username").getAsString();
        int musicId = payload.get("musicId").getAsInt();

        UserRepository userRepository = UserRepository.getInstance();
        MusicRepository musicRepository = MusicRepository.getInstance();

        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "User not found");
            return response;
        }

        Music music = musicRepository.getAllMusic().stream()
                .filter(m -> m.getId() == musicId)
                .findFirst()
                .orElse(null);

        if (music == null) {
            response.addProperty("status", Response.musicNotFound.toString());
            response.addProperty("message", "Music not found");
            return response;
        }

        try {
            if (user.getLikedSongs().contains(music.getId())) {
                response.addProperty("status", Response.alreadyLiked.toString());
                response.addProperty("message", "Song is already liked by the user");
            } else {
                user.getLikedSongs().add(music.getId());
                music.setLikeCount(music.getLikeCount() + 1);

                // Update the music's likeCount in the database
                musicRepository.updateMusic(music);

                userRepository.updateUser(user); // Ensure user is updated in the repository
                response.addProperty("status", Response.likeSuccess.toString());
                response.addProperty("message", "Song liked successfully");
            }
        } catch (Exception e) {
            response.addProperty("status", Response.likeFailed.toString());
            response.addProperty("message", "Failed to like song: " + e.getMessage());
        }

        return response;
    }

    private JsonObject handleDislikeSong(JsonObject payload) {
        JsonObject response = new JsonObject();
        String username = payload.get("username").getAsString();
        int musicId = payload.get("musicId").getAsInt();

        UserRepository userRepository = UserRepository.getInstance();
        MusicRepository musicRepository = MusicRepository.getInstance();

        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "User not found");
            return response;
        }

        Music music = musicRepository.getAllMusic().stream()
                .filter(m -> m.getId() == musicId)
                .findFirst()
                .orElse(null);

        if (music == null) {
            response.addProperty("status", Response.musicNotFound.toString());
            response.addProperty("message", "Music not found");
            return response;
        }

        try {
            if (!user.getLikedSongs().contains(music.getId())) {
                response.addProperty("status", Response.NotLiked.toString());
                response.addProperty("message", "Song is not liked by the user");
            } else {
                user.getLikedSongs().remove(Integer.valueOf(music.getId()));
                // Ensure like count doesn't go negative
                int newLikeCount = Math.max(0, music.getLikeCount() - 1);
                music.setLikeCount(newLikeCount);

                // Update the music's likeCount in the database
                musicRepository.updateMusic(music);
                userRepository.updateUser(user);

                response.addProperty("status", Response.dislikeSuccess.toString());
                response.addProperty("message", "Song disliked successfully");
            }
        } catch (Exception e) {
            response.addProperty("status", Response.dislikeFailed.toString());
            response.addProperty("message", "Failed to dislike song: " + e.getMessage());
        }

        return response;
    }

    private JsonObject handleGetUserPlaylists(JsonObject payload) {
        JsonObject response = new JsonObject();
        String username = payload.get("username").getAsString();

        UserRepository userRepository = UserRepository.getInstance();

        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "User not found");
            return response;
        }

        try {
            List<Playlist> userPlaylists = user.getPlaylists();
            List<JsonObject> playlistJsonList = new ArrayList<>();

            for (Playlist playlist : userPlaylists) {
                JsonObject playlistJson = new JsonObject();
                playlistJson.addProperty("id", playlist.getId());
                playlistJson.addProperty("name", playlist.getName());
                playlistJson.addProperty("description", playlist.getDescription());
                playlistJson.addProperty("createdDate", playlist.getCreatedDate().toString());
                playlistJson.addProperty("ownerId", playlist.getOwnerID());
                playlistJsonList.add(playlistJson);
            }

            response.addProperty("status", Response.getUserPlaylistsSuccess.toString());
            response.add("Payload", gson.toJsonTree(playlistJsonList));
        } catch (Exception e) {
            response.addProperty("status", Response.getUserPlaylistsFailed.toString());
            response.addProperty("message", "Failed to retrieve playlists: " + e.getMessage());
        }

        return response;
    }
}