package com.example.demo.repository;

import com.example.demo.entity.QuestionEntity;
import com.example.demo.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<QuestionEntity, String> {
}

//@Repository
//public interface QuestionRepo extends JpaRepository<Question, Long> {
//    Question findAll(Question pageable);
//}


