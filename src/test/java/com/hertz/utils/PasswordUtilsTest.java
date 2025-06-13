package com.hertz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

class PasswordUtilsTest {

    @Test
    void testHashPasswordAndVerifyPassword() {
        String password = "mySecretPassword123";
        String hashedPassword = PasswordUtils.hashPassword(password);

        // Assert that the hashed password is not null and not empty
        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());

        // Assert that the original password verifies against the hashed password
        assertTrue(PasswordUtils.verifyPassword(hashedPassword, password));

        // Assert that a different password does not verify
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "wrongPassword"));
    }

    @Test
    void testVerifyPasswordWithInvalidHash() {
        // Test with a malformed hash. BCrypt.checkpw expects a specific format.
        String invalidHash = "invalid-hash";
        String password = "somePassword";

        //BCrypt.checkpw will throw IllegalArgumentException, which PasswordUtils.verifyPassword catches
        assertFalse(PasswordUtils.verifyPassword(invalidHash, password));

        //Test with null hash. Should also handle it gracefully.
        assertFalse(PasswordUtils.verifyPassword(null, password));

        //Test with an empty hash.
        assertFalse(PasswordUtils.verifyPassword("", password));
    }

    @Test
    void testEmptyPassword() {
        // Empty string should throw an IllegalArgumentException in hashPassword
        Executable hashingEmptyString = () -> PasswordUtils.hashPassword("");
        assertThrows(IllegalArgumentException.class, hashingEmptyString, "Expected hashPassword to throw IllegalArgumentException for an empty password");

        String hashedPassword = "$2a$10$N9qo8uLOickQDq92N6EOeYiJHmxsGGtboYczHWqZ7V5FaKZXpdLhO";  // some valid hashed password.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "")); //Verifying empty password.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "notEmpty"));
    }

    @Test
    void testNullPassword() {
        // Null password should throw an IllegalArgumentException in hashPassword
        Executable hashingNull = () -> PasswordUtils.hashPassword(null);
        assertThrows(IllegalArgumentException.class, hashingNull, "Expected hashPassword to throw IllegalArgumentException for a null password");

        String hashedPassword = "$2a$10$N9qo8uLOickQDq92N6EOeYiJHmxsGGtboYczHWqZ7V5FaKZXpdLhO";  // some valid hashed password.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, null)); // Verify what verifyPassword does with null input.
        assertFalse(PasswordUtils.verifyPassword(hashedPassword, "a"));
    }

    @Test
    void testHashPasswordThrowsIllegalArgumentException() {
        //This test is redundant but good to emphasize the intent
        assertThrows(IllegalArgumentException.class, () -> PasswordUtils.hashPassword(null));
        assertThrows(IllegalArgumentException.class, () -> PasswordUtils.hashPassword(""));
    }
}
