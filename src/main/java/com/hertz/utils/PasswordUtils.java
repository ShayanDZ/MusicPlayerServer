package com.hertz.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()) throw new IllegalArgumentException();
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String hashedPassword, String rawPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty() || rawPassword == null || rawPassword.isEmpty())
            return false;
        try {
            boolean result = BCrypt.checkpw(rawPassword, hashedPassword);
            System.out.println("Password verification result: " + result);
            return result;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid password hash format: " + e.getMessage());
            return false;
        }
    }
}