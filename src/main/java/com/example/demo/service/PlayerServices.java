package com.example.demo.service;

import com.example.demo.constant.PlayerStatus;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PlayerServices {

    @Autowired
    private PlayerRepository playerRepository;


    public PlayerEntity createPlayer(String userId, PlayerStatus status) {
        PlayerStatus playerStatus = (status != null) ? status : PlayerStatus.DRAWS;
        PlayerEntity newPlayer = new PlayerEntity(userId, new ArrayList<>(), playerStatus);
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    public void changePlayerStatus(String userId, PlayerStatus status){
        Optional<PlayerEntity> player = playerRepository.findByUserId(userId);
        if (player.isPresent()) {
            PlayerEntity playerToUpdate = player.get();
            playerToUpdate.setStatus(status);
            playerRepository.save(playerToUpdate);
        }
    }

    public PlayerEntity findPlayerById(String playerId){
        Optional<PlayerEntity> player = playerRepository.findById(playerId);
        return player.get();
    }

    public PlayerEntity findPlayerByUserIdAndStatus(String userId, PlayerStatus status){
        Optional<PlayerEntity> player = playerRepository.findByUserIdAndStatus(userId, status);
        if (player.isPresent()){
            return player.get();
        } else return null;
    }

//    public PlayerEntity addAnswer (String answer){
//        Optional<PlayerEntity> existingAnswers = playerRepository.findByUserIdAndStatus(userId, status);
//        if (player.isPresent()){
//            return player.get();
//        } else return null;
//    }




}
