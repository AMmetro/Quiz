package com.example.demo.exception;

/**
 * Кастомный расширенный exeption
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException (String message){
        super(message);
    }
}
