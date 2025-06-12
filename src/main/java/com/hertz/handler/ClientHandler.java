package com.hertz.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hertz.model.Music;
import com.hertz.model.Response;
import com.hertz.model.User;
import com.hertz.repository.UserRepository;
import com.hertz.utils.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;

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
        String fullname = payload.get("fullname").getAsString();
        String username = payload.get("username").getAsString();
        String email = payload.get("email").getAsString();
        String password = payload.get("password").getAsString();
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
        User user = new User(username, email, fullname, password, LocalDate.now(), 0);
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
        String username = payload.get("username").getAsString();
        String password = payload.get("password").getAsString();
        User temp = UserRepository.getInstance().getAllUser().stream().findAny()
                .filter(user -> user.getUsername().equals(username) && verifyPassword(user.getHashedPassword(),password))
                .orElse(null);
        boolean test = UserRepository.getInstance().getAllUser().stream()
                .anyMatch(user -> user.getUsername().equals(username) && !verifyPassword(user.getHashedPassword(),password));

        JsonObject response = new JsonObject();
        if (temp!=null) {
            response.addProperty("status", Response.logInSuccess.toString());
            response.addProperty("message", "User logged in successfully");
        } else if(test){
            response.addProperty("status", Response.incorrectPassword.toString());
            response.addProperty("message", "Password is Incorrect");
        }
        else {
            response.addProperty("status", Response.userNotFound.toString());
            response.addProperty("message", "Invalid username");
        }
        return response;
    }


}