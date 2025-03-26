package com.example.demo.controller;

import com.example.demo.annotation.CurrentUserId;
import com.example.demo.dto.ErrorResponse;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.model.GameConnectRequest;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pair-game-quiz/pairs")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/connection")
    public ResponseEntity<?> connectUserToGame(@CurrentUserId String userId) {
        try {
            gameService.createGamePair(userId);
            return ResponseEntity.ok("Input data is accepted. Email with confirmation code will be send to passed email address.");
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
