package com.example.demo.service;

import com.example.demo.constant.GameStatus;
import com.example.demo.entity.GameEntity;
import com.example.demo.entity.QuestionEntity;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private QuestionRepo questionRepo;

    /* Можно так - прямое внедрение зависимостей - устарело - нетестируется
     *    @Autowired
     *    public void setUserRepo(UserRepo userRepo) {
     *        GameService.userRepo = userRepo; }
     */

    public Integer createGamePair(String userId) throws UserNotFoundException {
        // 1 - checking the existing active game with the same user
        List<Object[]> userActiveGame = gameRepository.findByIdAndStatus(userId, GameStatus.ACTIVE);
        if (!userActiveGame.isEmpty()) {
            throw new UserNotFoundException("Active game with user all ready exist");
        }
        // 2 - checking the existing pending game with the same user
        List<Object[]> userPendingGame = gameRepository.findByIdAndStatus(userId, GameStatus.PENDING);
        if (!userPendingGame.isEmpty()) {
            throw new UserNotFoundException("Pending game with user all ready exist");
        }
        // 3 - checking the existing pending game with the other user
        List<Object[]> otherPendingGame = gameRepository.findByNotIdAndStatus(userId, GameStatus.PENDING);
          if (!otherPendingGame.isEmpty()) {
              // 3a join user to the first game in array
              List questions = questionRepo.getFiveRandomQuestion();

              List<String> Ids =  questions.stream()
                      .map(QuestionEntity::getId)
                      .toList();



//          }



        // Преобразуйте результат, например, достаньте GameEntity из gamesData
//        Object[] firstResult = gamesData.get(0); // Берём первый результат

//        System.out.printf("===========firstresult=========");
//        System.out.printf(Arrays.toString(firstResult));

//        GameEntity gameEntity = mapToGameEntity(firstResult); // Вы должны реализовать метод mapToGameEntity
        return 1;
    }


}
