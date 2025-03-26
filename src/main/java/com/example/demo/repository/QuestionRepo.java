package com.example.demo.repository;

import com.example.demo.constant.GameStatus;
import com.example.demo.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends PagingAndSortingRepository<QuestionEntity, String> {

    Page<QuestionEntity> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM question_table ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<QuestionEntity> getFiveRandomQuestion();
}


