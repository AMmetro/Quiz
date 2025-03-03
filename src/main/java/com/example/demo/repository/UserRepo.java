package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


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
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    /*
    * JBQL для более точноого запроса
    * @Query("SELECT u FROM user_entity_table WHERE u.username = ?1")
    */
    UserEntity findByUsername(String username);
}

