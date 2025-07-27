package com.hertz.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateParserTest {

    @Test
    void testParseIso8601Date_ValidDate() {
        String validDate = "2023-10-01T12:34:56.789";
        LocalDateTime expectedDate = LocalDateTime.of(2023, 10, 1, 12, 34, 56, 789000000);
        LocalDateTime parsedDate = DateParser.parseIso8601Date(validDate);
        assertEquals(expectedDate, parsedDate, "The parsed date should match the expected date.");
    }

    @Test
    void testParseIso8601Date_InvalidDate() {
        String invalidDate = "0000-01-01T00:00:00.000";
        LocalDateTime fallbackDate = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime parsedDate = DateParser.parseIso8601Date(invalidDate);
        assertEquals(fallbackDate, parsedDate, "The parsed date should fall back to the default date.");
    }

    @Test
    void testParseIso8601Date_EmptyString() {
        String emptyDate = "";
        LocalDateTime fallbackDate = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime parsedDate = DateParser.parseIso8601Date(emptyDate);
        assertEquals(fallbackDate, parsedDate, "The parsed date should fall back to the default date for empty input.");
    }

    @Test
    void testParseIso8601Date_NullInput() {
        String nullDate = null;
        LocalDateTime fallbackDate = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime parsedDate = DateParser.parseIso8601Date(nullDate);
        assertEquals(fallbackDate, parsedDate, "The parsed date should fall back to the default date for null input.");
    }
}