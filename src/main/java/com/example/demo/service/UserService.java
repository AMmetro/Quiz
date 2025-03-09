package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.util.security.JwtTokenService;
import com.example.demo.util.security.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordService passwordService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    public UserService(PasswordService passwordService, JwtTokenService jwtTokenService) {
        this.passwordService = passwordService;
        this.jwtTokenService = jwtTokenService;
    }

    public UserEntity create(UserRequest userRequest) throws UserAlreadyExistException {
        String userName = userRequest.getUsername();
        UserEntity existingUser = userRepo.findByUsername(userName);

        if (existingUser != null) {
            throw new UserAlreadyExistException("пользователь " + userRequest.getUsername() + " уже существуюет");
        }

//todo - check if email have valid format

        UserEntity newUser = new UserEntity();
        newUser.setUsername(userRequest.getUsername());
        newUser.setAge(userRequest.getAge());
        newUser.setDob(userRequest.getAge());
        passwordService.setPassword(newUser, userRequest.getPassword());

        return userRepo.save(newUser);
    }

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        String userName = user.getUsername();
        UserEntity existingUser = userRepo.findByUsername(userName);
        if (existingUser != null) {
            throw new UserAlreadyExistException("пользователь " + user.getUsername() + " уже существуюет");
        }
        user.setDob(user.getAge());
        return userRepo.save(user);
    };

    public User findUser(Long id) throws UserNotFoundException {
        Optional<UserEntity> userDB = userRepo.findById(id);
        if (!!userDB.isPresent()) {
            /**  применить .get() к ненайденному user вызывает exeption
             */
            return User.toModelMapper(userDB.get());
        }
        /** кастомное исключение его нужно или сразу перехватить тут или пробросить родителю
         */
        throw new UserNotFoundException("пользователь c Id: " + id + " не найден");
    }

 /**
  * Transactional - транзакция, если ошибка то откат и возвращает не кастомную ошибку,
  * а ошибку о транзакции в целом
  */
@Transactional
    public UserEntity updateUser(Long id, Long age) throws IllegalStateException {
        /*
         * В контексте Transactional выводит в консоль ошибку и прерывается
         */
        UserEntity user = userRepo.findById(id).orElseThrow(() -> new IllegalStateException(
                "user with id= " + id + "does not exist")
        );
        if (age != null && !Objects.equals(user.getAge(), age)) {
            user.setAge(age);
        }
        userRepo.save(user);
        return user;
    }

    public Long deleteUser(Long id) throws UserNotFoundException {
        boolean exist = userRepo.existsById(id);
        if (!exist) {
            throw new IllegalStateException("user with id= " + id + "does not exist");
        }
        userRepo.deleteById(id);
        return id;
    }

    public LoginResponse login(LoginRequest loginRequest) throws UserNotFoundException {

        UserEntity user = userRepo.findByUsername(loginRequest.getUsername());

        System.out.println("--------user-------------");
        System.out.println(user);

        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + loginRequest.getUsername());
        }

        System.out.println("---333333333-------");

        if (!passwordService.isPasswordValid(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        System.out.println("---4444444444-------");
        System.out.println(user.getUsername());

//        if (user.isPasswordExpired()) {
//            throw new IllegalStateException("Password has expired");
//        }

        String token = jwtTokenService.generateToken(user.getUsername());

        System.out.println("---444444-------");
        System.out.println(token);
        System.out.println("------55555----------");

        return new LoginResponse(token, user.getUsername());
    }
}
