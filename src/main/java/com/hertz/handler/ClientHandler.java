package com.hertz.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hertz.model.User;
import com.hertz.repository.UserRepository;
import com.hertz.utils.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;

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
            String inputLine;

            // Read the request from the client
            while ((inputLine = in.readLine()) != null) {
                requestBuilder.append(inputLine);
                if (inputLine.isEmpty()) { // End of request
                    break;
                }
            }

            String requestString = requestBuilder.toString();
            System.out.println("Request received: " + requestString);

            // Parse the JSON request using GSON
            JsonObject requestJson = gson.fromJson(requestString, JsonObject.class);
            String action = requestJson.get("Request").getAsString();

            JsonObject responseJson;

            switch (action) {
                case "signUp":
                    responseJson = handleSignUp(requestJson.getAsJsonObject("Payload"));
                    break;
                case "logIn":
                    responseJson = handleLogIn(requestJson.getAsJsonObject("Payload"));
                    break;
                default:
                    responseJson = new JsonObject();
                    responseJson.addProperty("status", "error");
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
        String fullname = payload.get("fullname").getAsString();
        String username = payload.get("username").getAsString();
        String email = payload.get("email").getAsString();
        String password = payload.get("password").getAsString();
        User user = new User(username, email, password, fullname, LocalDate.now(), 0);
        UserRepository userRepository = UserRepository.getInstance();
        String responseMessage = UserRepository.getInstance().addUser(user);
        if (!"User added successfully".equals(responseMessage)) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("status", "error");
            errorResponse.addProperty("message", responseMessage);
            return errorResponse;
        }
        userRepository.getAllUser().add(user);
        response.addProperty("status", "success");
        response.addProperty("message", "User signed up successfully");
        return response;
    }

    private JsonObject handleLogIn(JsonObject payload) {
        String username = payload.get("username").getAsString();
        String password = payload.get("password").getAsString();
        String hashedPassword = passwordHasher(password);
        User temp = UserRepository.getInstance().getAllUser().stream().findAny()
                .filter(user -> user.getUsername().equals(username) && user.getHashedPassword().equals(hashedPassword))
                .orElse(null);

        JsonObject response = new JsonObject();
        if (temp!=null) {
            response.addProperty("status", "success");
            response.addProperty("message", "User logged in successfully");
        } else {
            response.addProperty("status", "error");
            response.addProperty("message", "Invalid username or password");
        }
        return response;
    }

    private String passwordHasher(String password) {
        //TODO
        return password;
    }
}