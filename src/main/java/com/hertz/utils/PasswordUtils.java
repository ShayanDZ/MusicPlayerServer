package com.hertz.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String hashedPassword, String rawPassword) {
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid password hash format: " + e.getMessage());
            return false; // Return false if the hash is invalid
        }
    }
}