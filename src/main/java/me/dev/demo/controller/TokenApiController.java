package me.dev.demo.controller;

import lombok.RequiredArgsConstructor;
import me.dev.demo.config.TokenAuthenticationFilter;
import me.dev.demo.config.jwt.TokenProvider;
import me.dev.demo.domain.RefreshToken;
import me.dev.demo.domain.User;
import me.dev.demo.dto.*;
import me.dev.demo.repository.RefreshTokenRepository;
import me.dev.demo.repository.UserRepository;
import me.dev.demo.service.MakeTokenService;
import me.dev.demo.service.UserService;
import java.util.Optional;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TokenApiController {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final MakeTokenService tokenService;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @GetMapping("/access")
    public ResponseEntity<?> getUserWithToken(@RequestHeader("Authorization") String authHeader)
    throws Exception
    {
        String accessToken = tokenProvider.getAccessToken(authHeader);

        if (tokenProvider.validToken(accessToken)) {
            String email = tokenProvider.getUserEmail(accessToken);// 토큰에서 유저 정보 추출
            return ResponseEntity.ok("인증된 사용자: " + email);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다");
        }
    }



    //"리프레시 토큰이 유효할때 새로운 액세스 토큰을 발급한다"
    @GetMapping("/newRefreshToken")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(CreateAccessTokenRequest request)
    throws Exception{
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
