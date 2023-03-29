package com.example.mungmung.mungWiki.repository;

import com.example.mungmung.mungWiki.entity.BookMark;
import com.example.mungmung.mungWiki.entity.DogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookMartRepository extends JpaRepository<BookMark,Long> {

    @Query("select b from BookMark b join fetch b.member bm join fetch b.mungWiki bw where (bm.id = :memberId) and (bw.dogType = :dogType)")
    Optional<BookMark> findByMemberIDAndDogType(Long memberId , DogType dogType);
}
