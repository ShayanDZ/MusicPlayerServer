package com.hertz.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EmailUtilsTest {

    @Test
    public void testSendEmail() {
        // use any email of yours to check
        String to = "shayan68dashtizad92@gmail.com";

        String subject = "Test Email";
        String body = "This is a test email sent from the EmailUtils unit test.";

        // Act & Assert
        assertDoesNotThrow(() -> EmailUtils.sendEmail(to, subject, body));
    }
}