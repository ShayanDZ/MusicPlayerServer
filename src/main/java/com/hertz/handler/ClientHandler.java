package com.hertz.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hertz.model.*;
import com.hertz.repository.UserRepository;
import com.hertz.utils.DatabaseConnection;
import com.hertz.utils.PasswordUtils;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
        User user = new User(username, email, fullname, hashedPassword, LocalDate.now(), 0);
        Response responseMessage = UserRepository.getInstance().addUser(user);
        if (!Response.signUpSuccess.equals(responseMessage)) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("status", responseMessage.toString());
            errorResponse.addProperty("message", responseMessage.toString());
            return errorResponse;
        }
        userRepository.getAllUser().add(user);
        response.addProperty("status", Response.signUpSuccess.toString());
        response.addProperty("message", "User signed up successfully");
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
        int userId = payload.get("userId").getAsInt();
        JsonObject musicMap = payload.getAsJsonObject("musicMap");
        String base64Data = payload.get("base64Data").getAsString();

        UserRepository userRepository = UserRepository.getInstance();
        User user = userRepository.getAllUser().stream()
                .filter(u -> u.getId() == userId)
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
            String artistBio = artistMap.has("bio") ? artistMap.get("bio").getAsString() : "Default bio";
            String artistProfileImageUrl = artistMap.has("profileImageUrl") ? artistMap.get("profileImageUrl").getAsString() : "Default image URL";
            List<String> artistGenres = artistMap.has("genres")
                    ? Arrays.asList(artistMap.get("genres").getAsString().split(","))
                    : new ArrayList<>();
            Artist artist = new Artist(artistName, artistBio, artistProfileImageUrl, artistGenres);

            // Parse album from musicMap
            JsonObject albumMap = musicMap.getAsJsonObject("album");
            String albumTitle = albumMap.get("title").getAsString();
            LocalDate albumReleaseDate = LocalDate.parse(albumMap.get("releaseDate").getAsString());
            String albumCoverImageUrl = albumMap.has("coverImageUrl") ? albumMap.get("coverImageUrl").getAsString() : "Default cover image URL";
            String albumDescription = albumMap.has("description") ? albumMap.get("description").getAsString() : "Default description";
            String albumGenre = albumMap.get("genre").getAsString();
            Album album = new Album(albumTitle, artist, albumReleaseDate, albumCoverImageUrl, albumGenre, albumDescription);

            // Parse music from musicMap
            String title = musicMap.get("title").getAsString();
            String genre = musicMap.get("genre").getAsString();
            int durationInSeconds = musicMap.get("durationInSeconds").getAsInt();
            LocalDate releaseDate = LocalDate.parse(musicMap.get("releaseDate").getAsString());
            int id = musicMap.get("id").getAsInt();
            String extension = musicMap.get("extension").getAsString();
            Music music = new Music(title, artist, genre, durationInSeconds, releaseDate, album, id, extension, base64Data);

            // Add music to user's tracks
            user.getTracks().add(music);

            // Save music to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            Document musicDocument = new Document("title", title)
                    .append("artist", artist)
                    .append("genre", genre)
                    .append("durationInSeconds", durationInSeconds)
                    .append("releaseDate", java.util.Date.from(releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .append("album", album)
                    .append("id", id)
                    .append("extension", extension)
                    .append("base64Data", base64Data); // Store base64 data if needed
            databaseConnection.getDatabase().getCollection("musics").insertOne(musicDocument);

            response.addProperty("status", Response.uploadMusicSuccess.toString());
            response.addProperty("message", "Music uploaded successfully");
        } catch (Exception e) {
            response.addProperty("status", Response.uploadMusicFailed.toString());
            response.addProperty("message", "Failed to upload music: " + e.getMessage());
        }

        return response;
    }

}