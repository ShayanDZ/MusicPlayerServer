package com.hertz.repository;

import com.hertz.model.ResetCode;

import java.util.HashMap;
import java.util.Map;

public class ResetCodeRepository {
    private static ResetCodeRepository instance;
    private final Map<String, ResetCode> codeStore = new HashMap<>();
    private ResetCodeRepository() {
    }
    public static ResetCodeRepository getInstance() {
        if (instance == null) {
            instance = new ResetCodeRepository();
        }
        return instance;
    }
    public synchronized void saveResetCode(ResetCode resetCode) {
        codeStore.put(resetCode.getEmail(), resetCode);
    }

    public synchronized ResetCode findByEmail(String email) {
        return codeStore.get(email);
    }

    public synchronized void deleteResetCode(String email) {
        codeStore.remove(email);
    }
}