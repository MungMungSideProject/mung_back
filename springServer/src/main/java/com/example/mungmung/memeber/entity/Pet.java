package com.example.mungmung.memeber.entity;

import com.example.mungmung.mungWiki.entity.DogType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Pet {
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "profile_id")
//    private MemberProfile memberProfile;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Pet(Member member, String name, Long age, DogType dogType){
        this.member = member;
        this.name = name;
        this.age = age;
        this.dogType = dogType;
    }
}
