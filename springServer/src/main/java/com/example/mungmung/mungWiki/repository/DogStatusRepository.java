package com.example.mungmung.mungWiki.repository;

import com.example.mungmung.mungWiki.entity.DogStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogStatusRepository extends JpaRepository<DogStatus,Long> {
}
