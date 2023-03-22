package com.example.mungmung.mungWiki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
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
    private DogStatus dogStatus;

    @OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WikiImages> wikiImages= new ArrayList<>();
}
