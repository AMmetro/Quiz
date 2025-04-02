package com.example.demo.service;

import com.example.demo.constant.GameStatus;
import com.example.demo.constant.PlayerStatus;
import com.example.demo.entity.GameEntity;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.entity.QuestionEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.ElseGameExeption;
import com.example.demo.exception.GameNotFoundExeption;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.GModal;
import com.example.demo.model.Question;
import com.example.demo.model.game.GameModel;
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
import java.util.*;
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
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    /* Можно так - прямое внедрение зависимостей - устарело - нетестируется
     *    @Autowired
     *    public void setUserRepo(UserRepo userRepo) {
     *        GameService.userRepo = userRepo; }
     */

    public GameEntity getGameById(String gameId, String userId ) throws GameNotFoundExeption, ElseGameExeption {
        Optional<GameEntity> game = gameRepository.findById(gameId);

        if (game.isEmpty()){
            throw new GameNotFoundExeption ("game with id " + gameId + " not found");
        }

        String user1Id = game.get().getUser_1();
        String user2Id = game.get().getUser_2();

        if ( user2Id != null && !user1Id.equals(userId) && !user2Id.equals(userId)) {
            throw new ElseGameExeption("user with id " + userId + " tries to get pair in which not participant");
        } else

        if ( user2Id == null && !user1Id.equals(userId) ) {
            throw new ElseGameExeption("user with id " + userId + " tries to get pair in which not participant");
        }
        else {
            return game.get();}
    }


    public GameEntity getCurrentUnfinishedUserGame(String userId ) throws GameNotFoundExeption, ElseGameExeption {
        Optional<GameEntity> game = gameRepository.findByIdAndStatus(userId, GameStatus.PENDING);

        if (game.isEmpty()){
            throw new GameNotFoundExeption ("game with user id " + userId + " not found");
        }

        String user1Id = game.get().getUser_1();
        String user2Id = game.get().getUser_2();

        if ( user2Id != null && !user1Id.equals(userId) && !user2Id.equals(userId)) {
            throw new ElseGameExeption("user with id " + userId + " tries to get pair in which not participant");
        } else

        if ( user2Id == null && !user1Id.equals(userId) ) {
            throw new ElseGameExeption("user with id " + userId + " tries to get pair in which not participant");
        }
        else {
            return game.get();}
    }









    public GameModel createGamePair(String userId) throws ElseGameExeption {

        UserEntity requestUser = userRepo.findById(Long.valueOf(userId)).get();
        String requestUserLogin = requestUser.getLogin();

        // 1 - checking the existing active game with the same user
        Optional<GameEntity>  userActiveGame = gameRepository.findByIdAndStatus(userId, GameStatus.ACTIVE);

        if (userActiveGame.isPresent()) {
            throw new ElseGameExeption("Current user is already participating in active pair");
        }

        // 2 - checking the existing pending game with the same user
        Optional<GameEntity> userPendingGame = gameRepository.findByIdAndStatus(userId, GameStatus.PENDING);

        if (userPendingGame.isPresent()) {
            throw new ElseGameExeption("Pending game with user already exist");
        }

        // 3 - checking the existing pending game with the other user
        Optional<GameEntity> dbPendingGame = gameRepository.findByNotIdAndStatus(userId, GameStatus.PENDING);


        if (dbPendingGame.isPresent()) {
            // 3a - join user to the first game in array
            GameEntity pendingGame = dbPendingGame.get();
            String playerOneId = pendingGame.getUser_1();
            UserEntity userOneEntity = userRepo.findById(Long.parseLong(userId)).get();
            String playerOneLogin = userOneEntity.getLogin();

            playerServices.changePlayerStatus(playerOneId, PlayerStatus.ACTIVE);

            List<QuestionEntity> questions = questionRepo.getFiveRandomQuestion();

            List<String> questionIds = questions.stream()
                    .map(QuestionEntity::getId)
                    .collect(Collectors.toList());
            PlayerEntity playerTwo = playerServices.createPlayer(userId, PlayerStatus.ACTIVE);
            String playerTwoId = playerTwo.getId();

            pendingGame.setUser_2(userId);
            pendingGame.setPlayer_2(playerTwoId);
            pendingGame.setStatus(GameStatus.ACTIVE);
            pendingGame.setQuestions(questionIds);
            pendingGame.setPairCreatedAt(LocalDateTime.now());
            gameRepository.save(pendingGame);


            //-----------------------------------------------------------------------------------------------------------------------------

//            GameModel.PlayerProgress firstPlayerProgress1 = new GameModel.PlayerProgress(
//                    List.of(new GameModel.Answer("q1", "Correct", LocalDateTime.now())),
//                    new GameModel.Player(playerOneId, playerOneLogin),
//                    0
//            );

//            GameModel.PlayerProgress secondPlayerProgress1 = new GameModel.PlayerProgress(
//                    List.of(new GameModel.Answer("q2", "Correct", LocalDateTime.now())),
//                    new GameModel.Player("player2", "login2"),
//                    15
//            );

//            List<GameModel.ModelQuestion> questionsTest1 = List.of(
//                    new GameModel.ModelQuestion("q1", "What is Java?"),
//                    new GameModel.ModelQuestion("q2", "What is Spring?")
//            );

            GameModel activeGameModel = new GameModel(
                    pendingGame.getId(),
                    null, //firstPlayerProgress1,
                    null, //secondPlayerProgress1,
                    questions,
                    GameStatus.ACTIVE,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    null //LocalDateTime.now()
            );

//-----------------------------------------------------------------------------------------------------------------------

            return activeGameModel;

        } else {
           // 3b - create new Game with current user in pending status
            PlayerEntity playerOne = playerServices.createPlayer(userId, PlayerStatus.DRAWS);
            String playerOneId = playerOne.getId();
            GameEntity newPendingGame = new GameEntity(playerOneId, userId, new ArrayList<>(), playerOneId, userId, GameStatus.PENDING );
            gameRepository.save(newPendingGame);

            //-----------------------------------------------------------------------------------------------------------------------------

//            GameModel.PlayerProgress firstPlayerProgress = new GameModel.PlayerProgress(
//                    List.of(new GameModel.Answer("", "", LocalDateTime.now())),
//                    new GameModel.Player(playerOneId, requestUserLogin),
//                    0
//            );

//            GameModel.PlayerProgress secondPlayerProgress = new GameModel.PlayerProgress(
//                    List.of(new GameModel.Answer("", "", LocalDateTime.now())),
//                    new GameModel.Player("", ""),
//                    0
//            );

//            List<GameModel.ModelQuestion> questionsTest = List.of();

            GameModel pendingGameModel = new GameModel(
                    newPendingGame.getId(),
                    null, //firstPlayerProgress,
                    null, //secondPlayerProgress,
                    null, //questionsTest,
                    GameStatus.PENDING,
                    LocalDateTime.now(),
                    null,
                    null //LocalDateTime.now()
            );
//-----------------------------------------------------------------------------------------------------------------------
            return pendingGameModel;
        }
    }

}
