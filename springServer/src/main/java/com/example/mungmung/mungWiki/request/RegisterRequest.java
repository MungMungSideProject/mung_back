package com.example.mungmung.mungWiki.request;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String dogType;

    private Long intelligenceLevel;

    private Long sheddingLevel;

    private Long sociabilityLevel;

    private Long activityLevel;

    private Long indoorAdaptLevel;

    private Long totalStatus;

    private String documentation;
}
