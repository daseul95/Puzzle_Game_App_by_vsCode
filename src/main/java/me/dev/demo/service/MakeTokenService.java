package me.dev.demo.service;

import lombok.RequiredArgsConstructor;
import me.dev.demo.config.jwt.TokenProvider;
import me.dev.demo.domain.User;
import org.springframework.stereotype.Service;
import java.time.Duration;


@RequiredArgsConstructor
@Service
public class MakeTokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) throws Exception{
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}

