package com.example.demo.repository;

import com.example.demo.constant.GameStatus;
import com.example.demo.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, String> {

    @Query(value = """
        SELECT g.*,
               CASE WHEN g.user_1 = :userId THEN true ELSE false END AS is_user_1,
               CASE WHEN g.user_2 = :userId THEN true ELSE false END AS is_user_2
        FROM games g
        WHERE (g.user_1 = :userId OR g.user_2 = :userId)
          AND (:status IS NULL OR g.status = CAST(:status AS VARCHAR))
        """, nativeQuery = true)
    List<Object[]> findByIdAndStatus(@Param("userId") String userId, @Param("status") GameStatus status);

    @Query(value = """
        SELECT * 
        FROM games 
        WHERE (user_1 != :userId OR user_2 != :userId) 
          AND (:status IS NULL OR status = :status)
        """, nativeQuery = true)
    List<Object[]>  findByNotIdAndStatus( @Param("userId") String userId, @Param("status") GameStatus status);

}
