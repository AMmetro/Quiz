package com.example.demo.service;

import com.example.demo.constant.GameStatus;
import com.example.demo.constant.PlayerStatus;
import com.example.demo.dto.question.AnswerForQuestionRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.ElseGameExeption;
import com.example.demo.exception.GameNotFoundExeption;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.AnswerModal;
import com.example.demo.model.GModal;
import com.example.demo.model.Question;
import com.example.demo.model.game.GameModel;
import com.example.demo.repository.*;
import com.example.demo.utils.pagination.PaginationParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
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

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AnswerRepo answerRepo;

    /** Можно так - прямое внедрение зависимостей - устарело - нетестируется
     *    @Autowired
     *    public void setUserRepo(UserRepo userRepo) { GameService.userRepo = userRepo; }
     */


    public AnswerModal addAnswerForNextQuestion(String userId, String userAnswer) throws GameNotFoundExeption, ElseGameExeption {

        // check if Active player exist
        Optional<PlayerEntity> playerEntityOptional = Optional.ofNullable(playerServices.findPlayerByUserIdAndStatus(userId, PlayerStatus.ACTIVE));
        if (!playerEntityOptional.isPresent()){
            throw new GameNotFoundExeption("player with user id " + userId + "in Active status not found");
        }

        String playerId = playerEntityOptional.get().getId();
        Optional<List<String>> gameQuestions = Optional.ofNullable(gameRepository.findByPlayerId(playerId).get().getQuestions());

        PlayerEntity player = playerEntityOptional.get();
        List playerAnswers = player.getAnswers();

        // find question that is answered now
        Integer indexOfAnswer = playerAnswers.size();

        // check if already have 5 answers
        if (indexOfAnswer == 4) {
            throw new ElseGameExeption("Current user already have 5 answer");
        }

        String questionId = gameQuestions.get().get(indexOfAnswer);

        Optional<QuestionEntity> question = questionRepo.findById(questionId);

        Boolean answerStatus = question.get().getCorrectAnswers().contains(userAnswer);

        // add 1 point to player score if answer is correct
        if (answerStatus){
            player.setScore(player.getScore() + 1);
        }

        // сохранить в answers текущий ответ
        AnswerEntity answerEntity = new AnswerEntity(userAnswer, questionId, playerId, answerStatus);

        // save player with updated options
        String answerId = answerRepo.save(answerEntity).getId();
        playerAnswers.add(answerId);
        player.setAnswers(playerAnswers);
        playerRepository.save(player);

        AnswerModal result = AnswerModal.toModalMapper(questionId, answerStatus, String.valueOf(answerEntity.getCreatedAt()));

        return result;
    }


    public GameModel getActiveUserGame(String userId ) throws GameNotFoundExeption {

        Optional<UserEntity> userEntityOptional = userRepo.findById(Long.valueOf(userId));
        if (userEntityOptional.isEmpty()){
            throw new GameNotFoundExeption("user with id " + userId + " not found");
        }

        Optional<GameEntity> activeUserGameOptional = gameRepository.findUnfinishedById(userId);

        if (activeUserGameOptional.isEmpty()){
            throw new GameNotFoundExeption("active game with user id " + userId + " not found");
        }

          // game
          GameEntity activeGame = activeUserGameOptional.get();
          String activeGameId = activeGame.getId();
          List<String> questions = activeGame.getQuestions();

          //player
          String playerOneId = activeGame.getPlayer_1();
          String playerTwoId = activeGame.getPlayer_2();
          PlayerEntity playerOne = playerServices.findPlayerById(playerOneId);
          PlayerEntity playerTwo = playerServices.findPlayerById(playerTwoId);
          List playerOneAnswers = playerOne.getAnswers();
          List playerTwoAnswers = playerTwo.getAnswers();
          Integer playerOneScore = playerOne.getScore();
          Integer playerTwoScore = playerTwo.getScore();

        // users
          String userOneId = activeGame.getUser_1();
          String userTwoId = activeGame.getUser_2();
          Optional<UserEntity> userOneEntity = userRepo.findById(Long.valueOf(userOneId));
          Optional<UserEntity> userTwoEntity = userRepo.findById(Long.valueOf(userTwoId));
        String userOneLogin = userOneEntity.get().getLogin();
        String userTwoLogin = userTwoEntity.get().getLogin();

          GameModel.PlayerProgress firstPlayerProgres = new GameModel.PlayerProgress(
                    playerOneAnswers,
          new GameModel.Player(playerOneId, userOneLogin),
                  playerOneScore
          );

            GameModel.PlayerProgress secondPlayerProgres = new GameModel.PlayerProgress(
                    playerTwoAnswers,
                    new GameModel.Player(playerTwoId, userTwoLogin),
                    playerTwoScore
            );

        GameModel userActiveGameModel = new GameModel(
                activeGameId,
                firstPlayerProgres,
                secondPlayerProgres,
                questions,
                activeGame.getStatus(),
                activeGame.getPairCreatedAt(),
                activeGame.getStartGameDate(),
                null
        );
        return userActiveGameModel;
    }


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
        Optional<GameEntity> pendingGameOptional = gameRepository.findByNotIdAndStatus(userId, GameStatus.PENDING);


        if (pendingGameOptional.isPresent()) {
            // 3a - join user to the first game in array
            GameEntity gameToStart = pendingGameOptional.get();
            String playerOneId = gameToStart.getUser_1();
            UserEntity userOneEntity = userRepo.findById(Long.parseLong(userId)).get();
            String playerOneLogin = userOneEntity.getLogin();

            playerServices.changePlayerStatus(playerOneId, PlayerStatus.ACTIVE);

            List<QuestionEntity> questions = questionRepo.getFiveRandomQuestion();
            List<String> questionIds = questions.stream()
                    .map(QuestionEntity::getId)
                    .collect(Collectors.toList());
            PlayerEntity playerTwo = playerServices.createPlayer(userId, PlayerStatus.ACTIVE);
            String playerTwoId = playerTwo.getId();

            gameToStart.setUser_2(userId);
            gameToStart.setPlayer_2(playerTwoId);
            gameToStart.setStatus(GameStatus.ACTIVE);
            gameToStart.setQuestions(questionIds);
            gameToStart.setPairCreatedAt(LocalDateTime.now());
            gameToStart.setStartGameDate(LocalDateTime.now());
            gameRepository.save(gameToStart);

            GameModel activeGameModel = new GameModel(
                    gameToStart.getId(),
                    null,
                    null,
                    questionIds,
                    GameStatus.ACTIVE,
                    gameToStart.getPairCreatedAt(),
                    gameToStart.getStartGameDate(),
                    null
            );

            return activeGameModel;

        } else {
           // 3b - create new Game with current user in pending status
            PlayerEntity playerOne = playerServices.createPlayer(userId, PlayerStatus.DRAWS);
            String playerOneId = playerOne.getId();
            GameEntity newPendingGame = new GameEntity(
                    playerOneId,
                    userId,
                    new ArrayList<>(),
                    null,
                    null,
                    GameStatus.PENDING,
                    LocalDateTime.now()
                    );
            gameRepository.save(newPendingGame);

            GameModel pendingGameModel = new GameModel(
                    newPendingGame.getId(),
                    null,
                    null,
                    null,
                    GameStatus.PENDING,
                    newPendingGame.getPairCreatedAt(),
                    null,
                    null
            );
            return pendingGameModel;
        }
    }

}
