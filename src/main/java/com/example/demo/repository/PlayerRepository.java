package com.example.demo.repository;

import com.example.demo.constant.GameStatus;
import com.example.demo.entity.GameEntity;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {

    @Query("SELECT p FROM PlayerEntity p WHERE p.userId = :input" )
    Optional<PlayerEntity> findByUserId(@Param("input") String input);
}
