package com.example.mungmung.mungWiki.repository;

import com.example.mungmung.mungWiki.entity.DogType;
import com.example.mungmung.mungWiki.entity.MungWiki;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MungWikiRepository extends JpaRepository<MungWiki,Long> {

    @Query("select w from MungWiki w join fetch w.dogType where w.dogType = :dogType")
    Optional<MungWiki> findByDogType(DogType dogType);

    @Query("select w from MungWiki w order by w.Id desc")
    List<MungWiki> findListFirstPage(Pageable pageable);

    @Query("select w from MungWiki w where  w.Id < :wikiId order by w.dogType desc")
    List<MungWiki> findListNextPage(Pageable pageable, Long wikiId);
}
