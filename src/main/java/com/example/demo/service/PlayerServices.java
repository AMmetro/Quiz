package com.example.demo.service;

import com.example.demo.constant.PlayerStatus;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

}
