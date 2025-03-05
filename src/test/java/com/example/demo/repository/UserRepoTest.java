package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepoTest {

    @Autowired
    private UserRepo underTestUserRepo;

    @AfterEach
    void tearDown() {
        underTestUserRepo.deleteAll();
    }

    @Test
    void itShouldSaveUserAndCheckIfExists() {
        // given
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("password123");
        LocalDate dob = LocalDate.now().minusYears(25);
        user.setDob(25); // используем существующий метод для установки возраста
        
        // when
        UserEntity savedUser = underTestUserRepo.save(user);
        
        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testUser");
        assertThat(savedUser.getPassword()).isEqualTo("password123");
    }

    @Test
    void itShouldCheckIfUserDoesNotExistById() {
        // given
        Long nonExistentId = 999L;

        // when
        boolean exists = underTestUserRepo.existsById(nonExistentId);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void itShouldFindUserByUsername() {
        // given
        String username = "testUser";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("password123");
        underTestUserRepo.save(user);

        // when
        UserEntity foundUser = underTestUserRepo.findByUsername(username);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(username);
    }
}