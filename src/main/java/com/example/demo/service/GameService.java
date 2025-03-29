package com.example.demo.service;

import com.example.demo.constant.GameStatus;
import com.example.demo.constant.PlayerStatus;
import com.example.demo.entity.GameEntity;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.entity.QuestionEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Question;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.QuestionRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.utils.pagination.PaginationParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private PlayerServices playerServices;

    @Autowired
    private UserRepo userRepo;

    /* Можно так - прямое внедрение зависимостей - устарело - нетестируется
     *    @Autowired
     *    public void setUserRepo(UserRepo userRepo) {
     *        GameService.userRepo = userRepo; }
     */

    public GameEntity createGamePair(String userId) throws UserNotFoundException {
        // 1 - checking the existing active game with the same user

        Optional<GameEntity>  userActiveGame = gameRepository.findByIdAndStatus(userId, GameStatus.ACTIVE);

        if (userActiveGame.isPresent()) {
            throw new UserNotFoundException("Active game with user all ready exist");
        }

        // 2 - checking the existing pending game with the same user
        Optional<GameEntity> userPendingGame = gameRepository.findByIdAndStatus(userId, GameStatus.PENDING);

        if (userPendingGame.isPresent()) {
            throw new UserNotFoundException("Pending game with user all ready exist");
        }
        // 3 - checking the existing pending game with the other user
        Optional<GameEntity> otherPendingGame = gameRepository.findByNotIdAndStatus(userId, GameStatus.PENDING);

        if (otherPendingGame.isPresent()) {
            // 3a - join user to the first game in array
            List<QuestionEntity> questions = questionRepo.getFiveRandomQuestion();
            List<String> questionIds = questions.stream()
                    .map(QuestionEntity::getId)
                    .collect(Collectors.toList());
            String playerTwoId = playerServices.createPlayer(userId, PlayerStatus.ACTIVE);
            GameEntity newGameEntity = new GameEntity();
//            .... to do ...
        } else {
           // 3b - create new Game with current user in pending status
            String playerOneId = playerServices.createPlayer(userId, PlayerStatus.ACTIVE);

            GameEntity newGame = new GameEntity(playerOneId, userId, new ArrayList<>());


            System.out.printf("Active game with user already exists: %s%n", userActiveGame);

            return gameRepository.save(newGame);

        }
        return new GameEntity();
    }

}
