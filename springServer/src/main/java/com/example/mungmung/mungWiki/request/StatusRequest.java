package com.example.mungmung.mungWiki.request;

import com.example.mungmung.mungWiki.entity.DogStatus;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class StatusRequest {

    private Long intelligenceLevel;

    private Long sheddingLevel;

    private Long sociabilityLevel;

    private Long activityLevel;

    private Long indoorAdaptLevel;

    private String DogType;

    public DogStatus toDogStatus(){
        return new DogStatus(this.intelligenceLevel,this.sheddingLevel,this.sociabilityLevel,this.activityLevel,this.indoorAdaptLevel);
    }
}
