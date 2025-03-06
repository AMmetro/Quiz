//package com.example.demo.service;
//
//import com.example.demo.entity.UserEntity;
//import com.example.demo.exception.UserAlreadyExistException;
//import com.example.demo.exception.UserNotFoundException;
//import com.example.demo.model.User;
//import com.example.demo.repository.UserRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepo userRepo;
//
//    private UserService servicesUnderTest;
//
//    @BeforeEach
//    void setUp() {
//        /* Рефлексия
//        *  метод устанавливает значение приватного поля servicesUnderTest: {"userRepo", userRepo}
//        *  что бы подменить зависимости для изоляции тестов
//        */
//        servicesUnderTest = new UserService();
//        ReflectionTestUtils.setField(servicesUnderTest, "userRepo", userRepo);
//    }
//
//    @Test
//    void shouldFindUserById() throws UserNotFoundException {
//        // given
//        Long userId = 1L;
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(userId);
//        userEntity.setUsername("testUser");
//        userEntity.setAge(25L);
//        userEntity.setTodos(new ArrayList<>());
//
//        given(userRepo.findById(userId)).willReturn(Optional.of(userEntity));
//
//        // when
//        User user = servicesUnderTest.findUser(userId);
//
//        // then
//        assertThat(user).isNotNull();
//        assertThat(user.getId()).isEqualTo(userId);
//        assertThat(user.getUsername()).isEqualTo("testUser");
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserNotFound() {
//        // given
//        Long userId = 1L;
//        given(userRepo.findById(userId)).willReturn(Optional.empty());
//
//        // when/then
//        assertThatThrownBy(() -> servicesUnderTest.findUser(userId))
//                .isInstanceOf(UserNotFoundException.class);
//    }
//
//    @Test
//    void shouldRegisterNewUser() throws UserAlreadyExistException {
//        // given
//        UserEntity user = new UserEntity();
//        user.setUsername("newUser");
//        user.setAge(25L);
//        user.setTodos(new ArrayList<>());
//
//        given(userRepo.findByUsername(user.getUsername())).willReturn(null);
//        given(userRepo.save(any(UserEntity.class))).willReturn(user);
//
//        // when
//        UserEntity result = servicesUnderTest.registration(user);
//
//        // then
//        assertThat(result).isNotNull();
//        verify(userRepo).save(user);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserAlreadyExists() {
//        // given
//        UserEntity user = new UserEntity();
//        user.setUsername("existingUser");
//        user.setAge(25L);
//        user.setTodos(new ArrayList<>());
//
//        given(userRepo.findByUsername(user.getUsername())).willReturn(user);
//
//        // when/then
//        assertThatThrownBy(() -> servicesUnderTest.registration(user))
//                .isInstanceOf(UserAlreadyExistException.class);
//    }
//
//    @Test
//    void shouldDeleteUser() throws UserNotFoundException {
//        // given
//        Long userId = 1L;
//        given(userRepo.existsById(userId)).willReturn(true);
//
//        // when
//        Long result = servicesUnderTest.deleteUser(userId);
//
//        // then
//        assertThat(result).isEqualTo(userId);
//        verify(userRepo).deleteById(userId);
//    }
//}
