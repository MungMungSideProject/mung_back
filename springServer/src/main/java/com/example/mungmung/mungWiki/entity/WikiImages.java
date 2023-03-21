package com.example.mungmung.mungWiki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class WikiImages {

    @Id
    @Column(name = "Wiki_Image_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String originalImageName;

    @Column
    private String uploadImageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_Id")
    private MungWiki mungWiki;
}
