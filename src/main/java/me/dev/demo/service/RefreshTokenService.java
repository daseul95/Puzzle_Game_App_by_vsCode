package me.dev.demo.service;

import lombok.RequiredArgsConstructor;
import me.dev.demo.domain.RefreshToken;
import me.dev.demo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

// 전달 받은 리프레시 토큰으로 리프레시 토큰 객체를 검색해서 전달하는
// findByRefreshToken()메서드 있음

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}

































