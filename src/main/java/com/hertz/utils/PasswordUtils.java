package com.hertz.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String hashedPassword, String rawPassword) {
        try {
            System.out.println("Verifying password:");
            System.out.println("Raw password: " + rawPassword);
            System.out.println("Hashed password: " + hashedPassword);
            boolean result = BCrypt.checkpw(rawPassword, hashedPassword);
            System.out.println("Password verification result: " + result);
            return result;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid password hash format: " + e.getMessage());
            return false;
        }
    }
}