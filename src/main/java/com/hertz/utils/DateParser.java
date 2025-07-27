package com.hertz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    public static LocalDateTime parseIso8601Date(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            // Return fallback date for null or empty input
            return LocalDateTime.of(1970, 1, 1, 0, 0);
        }

        // Check for invalid year (e.g., "0000")
        if (dateString.startsWith("0000")) {
            return LocalDateTime.of(1970, 1, 1, 0, 0); // Fallback date
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME; // Matches ISO 8601 format
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Failed to parse date: " + e.getMessage());
            // Return fallback date for invalid input
            return LocalDateTime.of(1970, 1, 1, 0, 0);
        }
    }
}