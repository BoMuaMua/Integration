package com.example.RecruitNewPeople.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
