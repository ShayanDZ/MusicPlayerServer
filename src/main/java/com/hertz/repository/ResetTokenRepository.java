package com.hertz.repository;

import com.hertz.model.ResetToken;

import java.util.HashMap;
import java.util.Map;

public class ResetTokenRepository {
    private final Map<String, ResetToken> tokenStore = new HashMap<>();

    public void saveResetToken(ResetToken resetToken) {
        tokenStore.put(resetToken.getToken(), resetToken);
    }

    public ResetToken findByToken(String token) {
        return tokenStore.get(token);
    }

    public void deleteResetToken(String token) {
        tokenStore.remove(token);
    }
}