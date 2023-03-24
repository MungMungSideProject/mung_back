package com.example.mungmung.memeber.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MemberProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id", nullable = false)
    private Long id;

    @Column
    private String phoneNumber;

    @OneToMany(mappedBy = "memberProfile", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MemberPets> pets = new ArrayList<>();

}
