package com.example.demo.service;

import com.example.demo.entity.QuestionEntity;
import com.example.demo.model.Question;
import com.example.demo.utils.pagination.PaginationParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public static Integer createQuestions(PaginationParameters paginationParameters) {
        // Создаем объект Pageable, учитывая пагинацию и сортировку
//        PageRequest pageable = PageRequest.of(
//                paginationParameters.getPage()-1,
//                paginationParameters.getSize(),
//                Sort.by(Sort.Direction.fromString(paginationParameters.getSortDirection()), paginationParameters.getSortBy())
//        );

//        Page<QuestionEntity> questions = questionRepo.findAll(pageable);

//        return new Question(
//                questions.getTotalPages(),
//                paginationParameters.getPage(),
//                paginationParameters.getSize(),
//                questions.getTotalElements(),
//                questions.getContent()
//        );
        return 1;
    }

}
