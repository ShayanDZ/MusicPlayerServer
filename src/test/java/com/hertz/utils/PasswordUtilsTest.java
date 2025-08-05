package com.hertz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

class PasswordUtilsTest {

    @Test
    void testHashPasswordAndVerifyPassword() {
        String password = "mySecretPassword123";
        String hashedPassword = PasswordUtils.hashPassword(password);

        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());

        assertTrue(PasswordUtils.verifyPassword(hashedPassword, password));

        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "wrongPassword"));
    }

    @Test
    void testVerifyPasswordWithInvalidHash() {

        String invalidHash = "invalid-hash";
        String password = "somePassword";

        assertFalse(PasswordUtils.verifyPassword(invalidHash, password));

        assertFalse(PasswordUtils.verifyPassword(null, password));

        assertFalse(PasswordUtils.verifyPassword("", password));
    }

    @Test
    void testEmptyPassword() {

        Executable hashingEmptyString = () -> PasswordUtils.hashPassword("");
        assertThrows(IllegalArgumentException.class, hashingEmptyString, "Expected hashPassword to throw IllegalArgumentException for an empty password");

        String hashedPassword = "$2a$10$N9qo8uLOickQDq92N6EOeYiJHmxsGGtboYczHWqZ7V5FaKZXpdLhO";  // some valid hashed password.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "")); //Verifying empty password.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "notEmpty"));
    }

    @Test
    void testNullPassword() {

        Executable hashingNull = () -> PasswordUtils.hashPassword(null);
        assertThrows(IllegalArgumentException.class, hashingNull, "Expected hashPassword to throw IllegalArgumentException for a null password");

        String hashedPassword = "$2a$10$N9qo8uLOickQDq92N6EOeYiJHmxsGGtboYczHWqZ7V5FaKZXpdLhO";  // some valid hashed password.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, null)); // Verify what verifyPassword does with null input.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "a"));
    }

    @Test
    void testHashPasswordThrowsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> PasswordUtils.hashPassword(null));
        assertThrows(IllegalArgumentException.class, () -> PasswordUtils.hashPassword(""));
    }
    @Test
    void testToViewHashOfAPassword(){
        System.out.println("Hash of '$uper_@dmin_very_$ecret': " + PasswordUtils.hashPassword("$uper_@dmin_very_$ecret"));
    }
}
