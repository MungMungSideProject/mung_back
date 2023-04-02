package com.example.mungmung.memeber.request;

import com.example.mungmung.mungWiki.entity.DogType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class PetRegisterRequest {
    private String name;
    private Long age;
    private String dogType;

    public DogType enumDogType() {
        return DogType.valueOfDogStatus(dogType);
    }
}
