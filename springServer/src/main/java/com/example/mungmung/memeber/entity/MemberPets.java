package com.example.mungmung.memeber.entity;

import com.example.mungmung.mungWiki.entity.DogType;
import jakarta.persistence.*;

@Entity
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

}
