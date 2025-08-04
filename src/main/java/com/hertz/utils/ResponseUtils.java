package com.hertz.utils;

import com.google.gson.JsonObject;

public class ResponseUtils {
    public static JsonObject createResponse(String status, String message) {
        JsonObject response = new JsonObject();
        response.addProperty("status", status);
        response.addProperty("message", message);
        return response;
    }
}