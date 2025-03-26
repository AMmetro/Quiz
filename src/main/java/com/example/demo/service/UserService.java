package com.example.demo.service;

import com.example.demo.dto.login.LoginResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.QuestionEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Question;
import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.QuestionRepo;
import com.example.demo.util.security.JwtTokenService;
import com.example.demo.util.security.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    private final PasswordService passwordService;
    private final JwtTokenService jwtTokenService;

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;

    @Value("${jwt.accesExpiration}")
    private Long accesExpiration;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    public UserService(PasswordService passwordService, JwtTokenService jwtTokenService) {
        this.passwordService = passwordService;
        this.jwtTokenService = jwtTokenService;
    }

    public Map<String, Object> getAllUsers() {
        List<UserEntity> allUserDB = userRepo.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("pagesCount", 0);
        response.put("page", 0);
        response.put("pageSize", 0);
        response.put("totalCount", allUserDB.size());
        response.put("items", allUserDB);

        return response;
    }


    public User findUser(Long id) throws UserNotFoundException {
        Optional<UserEntity> userDB = userRepo.findById(id);
        if (userDB.isPresent()) {
            /**  применить .get() к ненайденному user вызывает exeption
             */
            return User.toModelMapper(userDB.get());
        }
        /** кастомное исключение его нужно или сразу перехватить тут или пробросить родителю
         */
        throw new UserNotFoundException("пользователь c Id: " + id + " не найден");
    }

    public User create(UserRequest userRequest) throws UserAlreadyExistException {
        String userEmail = userRequest.getEmail();
        Optional<UserEntity> existingUser = userRepo.findByEmailOrLogin(userEmail);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("пользователь c email " + userRequest.getEmail() + " уже существуюет");
        }
        UserEntity newUser = new UserEntity();
        newUser.setLogin(userRequest.getLogin());
        newUser.setEmail(userRequest.getEmail());
        newUser.setConfirmed(true);
        passwordService.setPassword(newUser, userRequest.getPassword());

        return User.toModelMapper(userRepo.save(newUser));
    }

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        String email = user.getEmail();
        UserEntity existingUser = userRepo.findByEmailOrLogin(email).get();
        if (existingUser != null) {
            throw new UserAlreadyExistException("пользователь " + user.getEmail() + " уже существуюет");
        }
        user.setDob(user.getAge());
        return userRepo.save(user);
    }

    ;


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
        Optional<UserEntity> user = userRepo.findByEmailOrLogin(loginRequest.getLoginOrEmail());
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found: " + loginRequest.getLoginOrEmail());
        }
        if (!passwordService.isPasswordValid(loginRequest.getPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        String accessToken = jwtTokenService.generateToken(user.get().getLogin(), String.valueOf(user.get().getId()), accesExpiration);
        String refreshToken = jwtTokenService.generateToken(user.get().getLogin(), String.valueOf(user.get().getId()), refreshExpiration);
        LoginResponse response = new LoginResponse(accessToken, refreshToken);
        return response;
    }

}
