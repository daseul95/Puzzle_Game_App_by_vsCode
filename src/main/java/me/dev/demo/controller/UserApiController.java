package me.dev.demo.controller;
import lombok.RequiredArgsConstructor;
import me.dev.demo.config.jwt.TokenProvider;
import me.dev.demo.domain.RefreshToken;
import me.dev.demo.domain.User;
import me.dev.demo.dto.CreateBothTokenResponse;
import me.dev.demo.repository.RefreshTokenRepository;
import me.dev.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

import java.net.URI;
import java.time.Duration;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final MakeTokenService tokenService;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/signup/user")
    public ResponseEntity<CreateBothTokenResponse> signup(@RequestBody AddUserRequest request) throws Exception {

        userService.save(request);

        User user = userService.findByEmail(request.getEmail());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String accessToken = tokenProvider.generateToken(user, Duration.ofMinutes(15));
        String refreshToken = tokenProvider.generateToken(user,Duration.ofDays(7));

        RefreshToken newToken = new RefreshToken(user.getId(), refreshToken);
        refreshTokenRepository.save(newToken);

        CreateBothTokenResponse response = new CreateBothTokenResponse(accessToken, refreshToken);
        URI location = URI.create("/signup/user/" +user.getEmail());
        return ResponseEntity.created(location).body(response);
    }

}