package com.example.mungmung.memeber.entity;

import com.example.mungmung.mungWiki.entity.DogType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class MemberPets {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pet_id", nullable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private Long age;

    @Column
    private DogType dogType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private MemberProfile memberProfile;

    public MemberPets(String name,Long age,DogType dogType){
        this.name = name;
        this.age = age;
        this.dogType = dogType;
    }
}
