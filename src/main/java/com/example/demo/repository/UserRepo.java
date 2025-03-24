package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 *  можно расшириться от одного из трех репо:
 *   1) PagingAndSortingRepository
 *   2) PagingAndSortingRepository
 *   3) JpaRepository
 * public interface UserRepo PagingAndSortingRepository CrudRepository<UserEntity, Long> {
 *    UserEntity findByUsername(String username);
 * }
 */

/*
 *  в типе сущность с которой будет работать и тип его идентификатора т.е. id
 */
@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    /*
    * JPQL для более точного запроса
    */
    @Query("SELECT u FROM UserEntity u WHERE u.email = :input OR u.login = :input")
    Optional<UserEntity> findByEmailOrLogin(@Param("input") String input);

}




