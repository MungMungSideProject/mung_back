package com.example.mungmung.mungWiki.repository;

import com.example.mungmung.mungWiki.entity.WikiDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikiDocumentRepository extends JpaRepository<WikiDocument,Long> {
}
