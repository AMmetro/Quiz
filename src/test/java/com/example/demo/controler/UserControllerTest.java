package com.example.demo.controler;

import com.example.demo.controller.UserController;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controllerUnderTest;

    private User testUser;
    private UserEntity testUserEntity;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testUserEntity = new UserEntity();
        testUserEntity.setId(1L);
        testUserEntity.setUsername("testUser");
    }

    @Test
    void shouldFindUserById() throws UserNotFoundException {
        // given
        Long userId = 1L;
        given(userService.findUser(userId)).willReturn(testUser);

        // when
        ResponseEntity response = (ResponseEntity) controllerUnderTest.getUser(userId);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(testUser);
    }

    @Test
    void shouldReturn400WhenUserNotFound() throws UserNotFoundException {
        // given
        Long nonExistentUserId = 999L;
/*
* Mockito - mock assertion that method permanently return exception for such condition
*/
        given(userService.findUser(nonExistentUserId))
                .willThrow(new UserNotFoundException("Пользователь не найден"));

        // when
        ResponseEntity response = (ResponseEntity) controllerUnderTest.getUser(nonExistentUserId);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Пользователь не найден");
    }

    @Test
    void shouldReturn400OnGeneralException() throws UserNotFoundException {
        // given
        given(userService.findUser(anyLong()))
                .willThrow(new RuntimeException("стандартная ошибка"));

        // when
        ResponseEntity response = (ResponseEntity) controllerUnderTest.getUser(1L);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("стандартная ошибка");
    }
}