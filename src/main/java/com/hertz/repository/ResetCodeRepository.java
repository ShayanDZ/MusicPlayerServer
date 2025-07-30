package com.hertz.repository;

import com.hertz.model.ResetCode;

import java.util.HashMap;
import java.util.Map;

public class ResetCodeRepository {
    private final Map<String, ResetCode> codeStore = new HashMap<>();

    public void saveResetCode(ResetCode resetCode) {
        codeStore.put(resetCode.getEmail(), resetCode);
    }

    public ResetCode findByEmail(String email) {
        return codeStore.get(email);
    }

    public void deleteResetCode(String email) {
        codeStore.remove(email);
    }
}