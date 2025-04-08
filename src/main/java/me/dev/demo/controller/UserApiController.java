package me.dev.demo.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

import me.dev.demo.service.MakeTokenService;
import me.dev.demo.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import me.dev.demo.dto.AddUserRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import me.dev.demo.service.MakeTokenService;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final MakeTokenService tokenService;

    @PostMapping("/signup/user")
    public void signup(@RequestBody AddUserRequest request) {
        userService.save(request);
    }

}