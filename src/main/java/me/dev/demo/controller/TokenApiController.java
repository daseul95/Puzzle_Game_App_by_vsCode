package me.dev.demo.controller;

import lombok.RequiredArgsConstructor;
import me.dev.demo.config.jwt.TokenProvider;
import me.dev.demo.domain.RefreshToken;
import me.dev.demo.domain.User;
import me.dev.demo.dto.CreateAccessTokenRequest;
import me.dev.demo.dto.CreateAccessTokenResponse;
import me.dev.demo.dto.CreateBothTokenRequest;
import me.dev.demo.dto.CreateBothTokenResponse;
import me.dev.demo.repository.RefreshTokenRepository;
import me.dev.demo.repository.UserRepository;
import me.dev.demo.service.MakeTokenService;
import me.dev.demo.service.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final MakeTokenService tokenService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/api/token")
    public ResponseEntity<CreateBothTokenResponse> createBothToken(@RequestBody CreateBothTokenRequest request){

        User user = userService.findByEmail(request.getEmail());
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(6));
        String refreshToken = tokenProvider.generateToken(user,Duration.ofDays(12));

        RefreshToken newToken = new RefreshToken(user.getId(), refreshToken);
        refreshTokenRepository.save(newToken);

        CreateBothTokenResponse response = new CreateBothTokenResponse(accessToken, refreshToken);
        return ResponseEntity.ok(response);

    }



    //"리프레시 토큰이 유효할때 새로운 액세스 토큰을 발급한다"
    @GetMapping("/api/valid")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
