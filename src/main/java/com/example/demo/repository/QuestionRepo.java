package com.example.demo.repository;

import com.example.demo.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends PagingAndSortingRepository<QuestionEntity, String> {
    Page<QuestionEntity> findAll(Pageable pageable);
}


