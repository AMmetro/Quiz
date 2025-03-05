package com.example.demo.controler;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
/**   DI is equal use this.userService = new UserService
*/
    private UserService userService;

    @PostMapping(value="registration")
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
/*                    return
*                "<!DOCTYPE html>"+
*                        "<html>"+
*                        "	<head><title>Hello world!</title></head>"+
*                        "	<body>Hello world!</body>"+
*                        "</html>"
*/
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("стандартная ошибка");
        }
    };

        @Transactional()
        @PatchMapping("/{id}")
           public ResponseEntity updateUser(
            @PathVariable Long id,
            /*
            * можно добавить необязательных несколько параметров в запрос
            */
            @RequestParam(required = false) Long age)
        {
        try {
            UserEntity user =  userService.updateUser(id, age);
            return ResponseEntity.ok(user);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(
            @PathVariable Long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("ok");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}










