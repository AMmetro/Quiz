package com.example.demo.controler;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity registration(@RequestBody UserEntity user) {
        try {
            userService.registration(user);
            return ResponseEntity.ok("пользователь " + user.getUsername() + " создан");
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("стандартная ошибка");
        }
    }

    @GetMapping()
    public Object getUser(@RequestParam Long id) {
        try {
            User user = userService.findUser(id);
            return ResponseEntity.ok(user);
//                    return
//                "<!DOCTYPE html>"+
//                        "<html>"+
//                        "	<head><title>Hello world!</title></head>"+
//                        "	<body>Hello world!</body>"+
//                        "</html>"
// ;
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("стандартная ошибка");
        }
    };

        @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        try {
            System.out.println("id---------------- " + id);
            userService.deleteUser(id);
            return ResponseEntity.ok("ok");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("стандартная ошибка");
        }
    }

}










