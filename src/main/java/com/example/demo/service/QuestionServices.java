package com.example.demo.service;

import com.example.demo.dto.QuestionDTO;
import com.example.demo.entity.QuestionEntity;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepo;
import com.example.demo.utils.pagination.PaginationParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServices {

    private final QuestionRepo questionRepo;

    @Autowired
    public QuestionServices(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }


//    public Question getAllQuestions(PaginationParameters paginationParameters) {
//        List<QuestionEntity> allQuestion = questionRepo.findAll();
//
//        Long totalCount = questionRepo.count();
//        int totalPages = (int) Math.ceil((double) totalCount / paginationParameters.getSize());
//
//        Question pageable = new Question(
//                totalPages,
//                paginationParameters.getPage(),
//                paginationParameters.getSize(),
//                totalCount,
//                allQuestion
//        );
//
//        return pageable;
//    }

    public Question getAllQuestions(PaginationParameters paginationParameters) {
        // Создаем объект Pageable, учитывая пагинацию и сортировку
        PageRequest pageable = PageRequest.of(
                paginationParameters.getPage()-1,
                paginationParameters.getSize(),
                Sort.by(Sort.Direction.fromString(paginationParameters.getSortDirection()), paginationParameters.getSortBy())
        );

        Page<QuestionEntity> questions = questionRepo.findAll(pageable);

        return new Question(
                questions.getTotalPages(),
                paginationParameters.getPage(),
                paginationParameters.getSize(),
                questions.getTotalElements(),
                questions.getContent()
        );
    }

    public QuestionEntity createQuestion(QuestionDTO question) {
        QuestionEntity newQuestion = new QuestionEntity(question.getBody(), question.getCorrectAnswers());
        QuestionEntity savedQuestion = questionRepo.save(newQuestion);
        return savedQuestion;
    }

    public QuestionEntity updateQuestion(String id, QuestionDTO question) throws UserNotFoundException {
        Optional<QuestionEntity> existingQuestion = questionRepo.findById(id);
        if (existingQuestion.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        QuestionEntity updatedQuestion = new QuestionEntity(question.getBody(), question.getCorrectAnswers());
        QuestionEntity savedQuestion = questionRepo.save(updatedQuestion);
        return savedQuestion;
    }

    public void publishQuestion(String id) throws UserNotFoundException {
        Optional<QuestionEntity> existingQuestion = questionRepo.findById(id);
        if (existingQuestion.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        QuestionEntity questionToUpdate = existingQuestion.get();
        questionToUpdate.setPublished(true);
        questionRepo.save(questionToUpdate);
//        Optional<QuestionEntity> updatedQuestion = questionRepo.updateQuestion(id);
    }

    public void deleteQuestion(String id) throws UserNotFoundException {
        Optional<QuestionEntity> deletedQuestion = questionRepo.findById(id);
        if (deletedQuestion.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        questionRepo.deleteById(id);
    }

}
