package com.example.demo.controller;

import com.example.demo.annotation.CurrentUserId;
import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.RegistrationRequest;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pair-game-quiz/pairs")

public class GameController {

    @Autowired
    private GameService gameService;


    @PostMapping("/connection")
    public ResponseEntity<?> connectUserToGame(@CurrentUserId Long userId) {
//    public ResponseEntity<?> connectUserToGame() {

        System.out.printf("-------------------------------userId--------------------------------");
        System.out.printf(String.valueOf(userId));

        return ResponseEntity.ok().build();

    }
//        try {
//            GameService.createQuestions(request);
//            return ResponseEntity.ok("Input data is accepted. Email with confirmation code will be send to passed email address.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new ErrorResponse(
//                    List.of(new ErrorResponse.ErrorMessage(e.getMessage(), "email"))
//            ));
//        }
//    }

}
