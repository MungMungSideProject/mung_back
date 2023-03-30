package com.example.mungmung.mungWiki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DogStatus {

    @Id
    @Column(name = "status_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long intelligenceLevel;

    @Column(nullable = false)
    private Long sheddingLevel;

    @Column(nullable = false)
    private Long sociabilityLevel;

    @Column(nullable = false)
    private Long activityLevel;

    @Column(nullable = false)
    private Long indoorAdaptLevel;

    @Column(nullable = false)
    private Long NumOfGranter;

    public DogStatus(Long intelligenceLevel, Long sheddingLevel, Long sociabilityLevel, Long activityLevel, Long indoorAdaptLevel){
        this.intelligenceLevel = intelligenceLevel;
        this.sheddingLevel = sheddingLevel;
        this.sociabilityLevel = sociabilityLevel;
        this.activityLevel = activityLevel;
        this.indoorAdaptLevel = indoorAdaptLevel;
    }
}
