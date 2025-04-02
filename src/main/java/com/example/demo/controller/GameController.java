package com.example.demo.controller;

import com.example.demo.annotation.CurrentUserId;
import com.example.demo.dto.ErrorResponse;
import com.example.demo.entity.GameEntity;
import com.example.demo.exception.ElseGameExeption;
import com.example.demo.exception.GameNotFoundExeption;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.model.GModal;
import com.example.demo.model.GameConnectRequest;
import com.example.demo.model.game.GameModel;
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
            GameModel game =  gameService.createGamePair(userId);
            return ResponseEntity.ok(game);
        } catch (ElseGameExeption e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/my-current")
    public ResponseEntity<?> getCurrentUnfinishedUserGame (@CurrentUserId String userId) {
        try {
            GameEntity game =  gameService.getCurrentUnfinishedUserGame(userId);
            return ResponseEntity.ok(game);
        }  catch (GameNotFoundExeption e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ElseGameExeption e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{gameId}")
    public ResponseEntity<?> getGameById (@PathVariable String gameId, @CurrentUserId String userId) {
        try {
            GameEntity game =  gameService.getGameById(gameId, userId);
            return ResponseEntity.ok(game);
        }  catch (GameNotFoundExeption e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ElseGameExeption e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
