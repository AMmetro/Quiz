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
            System.out.printf("cccccccccccccccccc");
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


        //-----------------------------------------------------------------------------------------------------------------------------

        GameModel.PlayerProgress firstPlayerProgress1 = new GameModel.PlayerProgress(
                List.of(new GameModel.Answer("q1", "Correct", LocalDateTime.now())),
                new GameModel.Player("player1", "login1"),
                10
        );

        GameModel.PlayerProgress secondPlayerProgress1 = new GameModel.PlayerProgress(
                List.of(new GameModel.Answer("q2", "Correct", LocalDateTime.now())),
                new GameModel.Player("player2", "login2"),
                15
        );

        List<GameModel.ModelQuestion> questionsTest1 = List.of(
                new GameModel.ModelQuestion("q1", "What is Java?"),
                new GameModel.ModelQuestion("q2", "What is Spring?")
        );

        GameModel gameTest1 = new GameModel(
                "id",
                firstPlayerProgress1,
                secondPlayerProgress1,
                questionsTest1,
//                "PendingSecondPlayer",
                GameStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

//-----------------------------------------------------------------------------------------------------------------------

        GModal gameTest2 = GModal.toModalMapper("334");

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 3 - checking the existing pending game with the other user
        Optional<GameEntity> otherPendingGame = gameRepository.findByNotIdAndStatus(userId, GameStatus.PENDING);

                                                        System.out.printf("333333333333333333 ");
                                                        System.out.printf(otherPendingGame.toString());
                                                        System.out.printf(" ======================");

        if (otherPendingGame.isPresent()) {
            // 3a - join user to the first game in array
            List<QuestionEntity> questions = questionRepo.getFiveRandomQuestion();

                                                                System.out.printf("4444444444444444 ");
                                                                System.out.printf(otherPendingGame.toString());
                                                                System.out.printf(" ======================");

            List<String> questionIds = questions.stream()
                    .map(QuestionEntity::getId)
                    .collect(Collectors.toList());
            PlayerEntity playerTwo = playerServices.createPlayer(userId, PlayerStatus.ACTIVE);
            String playerTwoId = playerTwo.getId();
//            String playerTwoLogin = playerTwo.

                                                    System.out.printf("77777777777777777 ");
                                                    System.out.printf(questionIds.toString());
                                                    System.out.printf(" ======================");

           GameEntity pendingGame = otherPendingGame.get();

            pendingGame.setUser_2(userId);
            pendingGame.setPlayer_2(playerTwoId);
            pendingGame.setStatus(GameStatus.ACTIVE);
            pendingGame.setQuestions(questionIds);


            System.out.printf("88888888888888888 ");
            System.out.printf(pendingGame.toString());

               gameRepository.save(pendingGame);

            System.out.printf("999999999999999999 ");
            System.out.printf(pendingGame.toString());

//            return  pendingGame;
            // должен вернуть большой обект
            return gameTest1;

        } else {
           // 3b - create new Game with current user in pending status
            PlayerEntity playerOne = playerServices.createPlayer(userId, PlayerStatus.ACTIVE);
            String playerOneId = playerOne.getId();




                                                                        System.out.printf("55555555555555555555 ");
                                                                        System.out.printf(playerOneId);
                                                                        System.out.printf(" ======================");

            GameEntity newGame = new GameEntity(playerOneId, userId, new ArrayList<>(), "111", "222");
//            System.out.printf("Active game with user already exists: %s%n", userActiveGame);
            gameRepository.save(newGame);
            // должен вернуть большой обект

            //-----------------------------------------------------------------------------------------------------------------------------

            GameModel.PlayerProgress firstPlayerProgress = new GameModel.PlayerProgress(
                    List.of(new GameModel.Answer("", "", LocalDateTime.now())),
                    new GameModel.Player(playerOneId, requestUserLogin),
                    0
            );

            GameModel.PlayerProgress secondPlayerProgress = new GameModel.PlayerProgress(
                    List.of(new GameModel.Answer("", "", LocalDateTime.now())),
                    new GameModel.Player("", ""),
                    0
            );

            List<GameModel.ModelQuestion> questionsTest = List.of(
//                    new GameModel.ModelQuestion("q1", "What is Java?"),
//                    new GameModel.ModelQuestion("q2", "What is Spring?")
            );

            GameModel gameTest = new GameModel(
                    newGame.getId(),
                    firstPlayerProgress,
                    secondPlayerProgress,
                    questionsTest,
                    GameStatus.PENDING,
                    LocalDateTime.now(),
                    null,
                    LocalDateTime.now()
            );

//-----------------------------------------------------------------------------------------------------------------------



            return gameTest;
        }
    }

}
