package com.example.demo.controler;

import com.example.demo.entity.TodoEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.model.Todo;
import com.example.demo.service.TodoService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("todos")
public class TodoControler {

    @Autowired
    private TodoService todoService;


    @PostMapping
    public ResponseEntity createTodo(@RequestBody TodoEntity todo,
                                     @RequestParam Long userId) {
        try {
            Todo newTodo =  todoService.createTodo(userId, todo);
            return ResponseEntity.ok(newTodo);
//        catch (UserAlreadyExistException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("стандартная ошибка");
        }
//        return  ResponseEntity.ok();
    }

    @PutMapping
    public ResponseEntity completeTodo(@RequestParam Long id){

//        String result = todoService.completeTodo(userId, todo);
//        System.out.println("result");
//        System.out.println(result);
        try {
//            todoService.completeTodo(id);
            return ResponseEntity.ok(todoService.completeTodo(id));
//        catch (UserAlreadyExistException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("стандартная ошибка");
        }
//        return  ResponseEntity.ok();

    }

}
