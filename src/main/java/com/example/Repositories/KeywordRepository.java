package com.example.Repositories;

import com.example.Entities.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword,Long> {
    Keyword findKeywordById(Long ig);
}
