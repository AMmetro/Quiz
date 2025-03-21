package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.QuestionEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.QuestionRepo;
import com.example.demo.util.security.JwtTokenService;
import com.example.demo.util.security.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordService passwordService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    public UserService(PasswordService passwordService, JwtTokenService jwtTokenService) {
        this.passwordService = passwordService;
        this.jwtTokenService = jwtTokenService;
    }

    public List<UserEntity> getAllUsers()  {
        List<UserEntity> allUserDB = userRepo.findAll();

//   return     {
//            "pagesCount": 0,
//                "page": 0,
//                "pageSize": 0,
//                "totalCount": 0,
//                "items": allUserDB
//        }

        return allUserDB;
    }


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

    public UserEntity create(UserRequest userRequest) throws UserAlreadyExistException {
        String userEmail = userRequest.getEmail();
        UserEntity existingUser = userRepo.findByEmail(userEmail);

        if (existingUser != null) {
            throw new UserAlreadyExistException("пользователь c email" + userRequest.getEmail() + " уже существуюет");
        }

//todo - check if email have valid format

        UserEntity newUser = new UserEntity();
        newUser.setLogin(userRequest.getLogin());
//        newUser.setUsername(userRequest.getUsername());
        newUser.setEmail(userRequest.getEmail());
//        newUser.setAge(55L);
//        newUser.setDob(userRequest.getAge());
        passwordService.setPassword(newUser, userRequest.getPassword());

        return userRepo.save(newUser);
    }

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        String email = user.getEmail();
        UserEntity existingUser = userRepo.findByEmail(email);
        if (existingUser != null) {
            throw new UserAlreadyExistException("пользователь " + user.getEmail() + " уже существуюет");
        }
        user.setDob(user.getAge());
        return userRepo.save(user);
    };


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
        UserEntity user = userRepo.findByEmail("tesl.ru");
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + loginRequest.getPassword());
        }
        if (!passwordService.isPasswordValid(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        String token = jwtTokenService.generateToken(user.getLogin());
        return new LoginResponse(token, user.getLogin());
    }

}
