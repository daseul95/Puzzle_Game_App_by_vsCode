package me.dev.demo.controller;

import me.dev.demo.config.jwt.TokenProvider;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dev.demo.dto.AuthResponse;
import me.dev.demo.dto.SignupRequest;
import me.dev.demo.service.UserService;
import me.dev.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    @GetMapping("/login")
    public String SignupLogin() {
        return "login";
    }
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute @Valid SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입 성공!");
    }
}