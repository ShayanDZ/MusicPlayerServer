package com.hertz.model;

import java.time.LocalDateTime;

public class ResetToken {
    private String username;
    private String token;
    private LocalDateTime expiryTime;

    public ResetToken(String username, String token, LocalDateTime expiryTime) {
        this.username = username;
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}