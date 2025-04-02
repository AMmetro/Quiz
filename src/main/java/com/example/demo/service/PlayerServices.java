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

    public PlayerEntity findPlayerByUserId(String userId){
        Optional<PlayerEntity> player = playerRepository.findByUserId(userId);
        return player.get();
    }

}
