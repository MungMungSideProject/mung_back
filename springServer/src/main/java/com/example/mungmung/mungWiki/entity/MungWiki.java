package com.example.mungmung.mungWiki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class MungWiki {

    @Id
    @Column(name = "wiki_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private DogType dogType;

    @OneToOne(fetch = FetchType.LAZY)
    private WikiDocument wikiDocument;

    @OneToOne(fetch = FetchType.LAZY)
    private MungWiki mungWiki;

    @OneToMany(fetch = FetchType.LAZY)
    private List<WikiImages> wikiImages= new ArrayList<>();
}
