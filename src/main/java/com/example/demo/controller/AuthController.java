package com.example.demo.controller;


import com.example.demo.dto.login.LoginResponse;
import com.example.demo.dto.RegistrationRequest;
import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import com.example.demo.util.security.JwtTokenService;
import com.example.demo.util.security.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordService passwordService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService,PasswordService passwordService, JwtTokenService jwtTokenService) {
        this.authService = authService;
        this.passwordService = passwordService;
        this.jwtTokenService = jwtTokenService;
    }

//    @Autowired
//    public UserService(PasswordService passwordService, JwtTokenService jwtTokenService) {
//        this.passwordService = passwordService;
//        this.jwtTokenService = jwtTokenService;
//    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("Input data is accepted. Email with confirmation code will be send to passed email address.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                List.of(new ErrorResponse.ErrorMessage(e.getMessage(), "email"))
            ));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse.ErrorMessage> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> new ErrorResponse.ErrorMessage(
                error.getDefaultMessage(),
                ((FieldError) error).getField()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }


    //    @PostMapping("login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
//        try {
//            LoginResponse response = userService.login(loginRequest);
//            return ResponseEntity.ok(response);
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (IllegalStateException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("An error occurred during login");
//        }
//    }


    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) throws UserNotFoundException {

        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response).getBody();

//                try {
//            LoginResponse response = userService.login(loginRequest);
//            return ResponseEntity.ok(response);
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.badRequest();
//        }



//        UserEntity user = userRepo.findByEmail(loginRequest.getEmail()).get();
//        if (user == null) {
//            throw new UserNotFoundException("User not found with email: " + loginRequest.getEmail());
//        }
//        if (!passwordService.isPasswordValid(loginRequest.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("Invalid password");
//        }
//        String token = jwtTokenService.generateToken(user.getLogin());
//        return new LoginResponse(token, user.getLogin());
    }




} 