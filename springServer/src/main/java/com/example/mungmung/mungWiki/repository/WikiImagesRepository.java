package com.example.mungmung.mungWiki.repository;

import com.example.mungmung.mungWiki.entity.WikiImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WikiImagesRepository extends JpaRepository<WikiImages,Long> {

    @Query("select w from WikiImages w join fetch w.mungWiki where w.mungWiki.Id = :mungWikiId")
    List<WikiImages> findImagesByWikiId(Long mungWikiId);
}
