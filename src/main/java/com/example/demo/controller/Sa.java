package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.question.PostQuestionRequest;
import com.example.demo.dto.question.PublishQuestionRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.QuestionEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.model.User;
import com.example.demo.service.QuestionServices;
import com.example.demo.service.UserService;
import com.example.demo.utils.pagination.PaginationParameters;
import com.example.demo.utils.pagination.SortQueryUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sa")
public class Sa {

    @Autowired
    private UserService userService;
    
    @Autowired
    private QuestionServices questionServices;


    @GetMapping(value = "users")
    public Object getUsers() {
        try {
            Map<String, Object> users = (Map<String, Object>) userService.getAllUsers();
            return ResponseEntity.ok(users);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "users")
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest userRequest) {
        try {
            User user = userService.create(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/quiz/questions")
    public ResponseEntity<?> getAllQuestions(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        PaginationParameters paginationParameters = SortQueryUtils.getPaginationParameters(pageNumber, pageSize, sortBy, sortDirection);
        try {
            return ResponseEntity.ok(questionServices.getAllQuestions(paginationParameters));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/quiz/questions")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody PostQuestionRequest question) {
        try {
            QuestionEntity result = questionServices.createQuestion(question);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/quiz/questions/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable String id, @Valid @RequestBody PostQuestionRequest question ) {
        try {
            QuestionEntity result = questionServices.updateQuestion(id, question);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/quiz/questions/{id}/publish")
    public ResponseEntity<?> publishQuestion(@PathVariable String id, @Valid @RequestBody PublishQuestionRequest question ) {
        try {
            questionServices.publishQuestion(id, question.getPublished() );
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/quiz/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
        try {
            questionServices.deleteQuestion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Вопрос с ID " + id + " не найден или произошла ошибка: " + e.getMessage());
        }
    }

    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}










