package com.example.demo.repository;

import com.example.demo.constant.GameStatus;
import com.example.demo.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, String> {

    @Query("SELECT g FROM GameEntity g WHERE (g.user_1 = :inputId OR g.user_2 = :inputId) AND g.status = :status")
    Optional<GameEntity> findByIdAndStatus(@Param("inputId") String inputId, @Param("status") GameStatus status);

    @Query("SELECT g FROM GameEntity g WHERE (g.user_1 != :inputId OR g.user_2 != :inputId) AND g.status = :status")
    Optional<GameEntity> findByNotIdAndStatus(@Param("inputId") String inputId, @Param("status") GameStatus status);

    @Query("SELECT g FROM GameEntity g WHERE (g.user_1 = :inputId OR g.user_2 = :inputId) AND g.status = 'ACTIVE'")
    Optional<GameEntity> findUnfinishedById(@Param("inputId") String inputId);

    @Query("SELECT g FROM GameEntity g WHERE (g.player_1 = :inputId OR g.player_2 = :inputId)")
    Optional<GameEntity> findByPlayerId(@Param("inputId") String inputId);
}
