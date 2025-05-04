package com.example.demo.repository;

import com.example.demo.constant.GameStatus;
import com.example.demo.constant.PlayerStatus;
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

    // Метод для поиска по userId и статусу
    @Query("SELECT p FROM PlayerEntity p WHERE p.userId = :input AND (:status IS NULL OR p.status = :status)")
    Optional<PlayerEntity> findByUserIdAndStatus(@Param("input") String input, @Param("status") PlayerStatus status);

    // Перегруженный метод, если статус не указан
    default Optional<PlayerEntity> findByUserId(String input) {
        return findByUserIdAndStatus(input, null); // Вызов основного метода с null
    }

}
