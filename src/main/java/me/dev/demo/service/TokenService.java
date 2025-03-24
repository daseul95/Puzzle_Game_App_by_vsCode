// 아직 crud작업만 하므로 토큰 서비스는 꺼놉니다

// package me.dev.demo.service;

// import lombok.RequiredArgsConstructor;
// import me.dev.demo.domain.User;
// import me.dev.demo.jwt.TokenProvider;
// import org.springframework.stereotype.Service;

// import java.time.Duration;



// /*전달받은리프레시 토큰으로 토큰 유효성을 검사하고
// * 유효한 토큰일 때 리프레시 토큰으로 사용자 ID를 찾음
// * 마지막으로는 사용자 ID로 토큰 제공자의 generateToken()메서드를 호출해서
// * 새로운 액세스 토큰을 생성함*/
// @RequiredArgsConstructor
// @Service
// public class TokenService {

//     private final TokenProvider tokenProvider;
//     private final RefreshTokenService refreshTokenService;
//     private final UserService userService;

//     public String createNewAccessToken(String refreshToken){
//         //토큰 유효성 검사에 실패하면 예외 발생
//         if(!tokenProvider.validToken(refreshToken)){
//             throw new IllegalArgumentException("unexpected token");
//         }

//         Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
//         User user = userService.findById(userId);

//         return tokenProvider.generateToken(user, Duration.ofHours(2));
//     }
// }
