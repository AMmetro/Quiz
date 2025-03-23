package com.example.demo.service;

import com.example.demo.dto.RegistrationRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Autowired
    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void register(RegistrationRequest request) throws UserAlreadyExistException {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with email " + request.getEmail() + " already exists");
        }

        String confirmationCode = generateConfirmationCode();

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmationCode(confirmationCode);
        user.setConfirmed(false);

        userRepo.save(user);

        sendConfirmationEmail(user.getEmail(), confirmationCode);
    }

    private String generateConfirmationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendConfirmationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Confirmation");
        message.setText("Your confirmation code is: " + code);
        mailSender.send(message);
    }
} 