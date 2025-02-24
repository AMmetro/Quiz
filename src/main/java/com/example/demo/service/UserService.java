package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        String userName = user.getUsername();
        UserEntity existingUser = userRepo.findByUsername(userName);
        if (existingUser != null) {
            throw new UserAlreadyExistException("пользователь " + user.getUsername() + " уже существуюет");
        }
        return userRepo.save(user);
    }

    ;

    public User findUser(Long id) throws UserNotFoundException {
        Optional<UserEntity> userDB = userRepo.findById(id);
        if (!!userDB.isPresent()) {
            /*
             *  применить .get() к ненайденному user вызывает exeption
             */
            return User.toModelMapper(userDB.get());
        }
        throw new UserNotFoundException("пользователь c Id: " + id + " не найден");
    }

    ;

    public Long deleteUser(Long id) throws UserNotFoundException {
        System.out.println("findUser(id);");
        System.out.println(findUser(id));
        userRepo.deleteById(id);
        return id;
        }


}
