package com.example.mungmung.memeber.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class MemberProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id", nullable = false)
    private Long id;

    @Column
    private String phoneNumber;

    @OneToMany(mappedBy = "memberProfile", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MemberPets> pets = new ArrayList<>();

    public MemberProfile(String phoneNumber, List<MemberPets> petsList){
        this.phoneNumber = phoneNumber;
        this.pets = petsList;
    }
}
