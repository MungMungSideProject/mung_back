package com.example.mungmung.walkingDog.entity;

import com.example.mungmung.utility.StringListConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
@Embeddable
public class WalkingDogInfo {

    @Convert(converter = StringListConverter.class)
    private String thumbnailPath;

    @Convert(converter = StringListConverter.class)
    private List<String> imagesName;
}
