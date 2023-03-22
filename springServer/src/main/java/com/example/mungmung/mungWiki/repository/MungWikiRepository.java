package com.example.mungmung.mungWiki.repository;

import com.example.mungmung.mungWiki.entity.DogType;
import com.example.mungmung.mungWiki.entity.MungWiki;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MungWikiRepository extends JpaRepository<MungWiki,Long> {

    @Query("select w from MungWiki w join fetch w.wikiImages where w.dogType = :dogtype")
    Optional<MungWiki> findByDogType(DogType dogType);
}
