package me.dev.demo.config.jwt;


import io.jsonwebtoken.Jwts;
import me.dev.demo.domain.User;
import me.dev.demo.jwt.JwtProperties;
import me.dev.demo.jwt.TokenProvider;
import me.dev.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class TokenProviderTest {


    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰 만들기")
    @Test
    void generateToken(){
        //given
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());
        //when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
        //then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id",Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }
    /*

    generateToken()
    given : 토큰에 유저 정보를 추가하기 위한 테스트 유저를 만듬
    when : 토큰 제공자의 generateToken()메서드를 호출해 토큰을 만듬
    then : jjwt 라이브러리를 사용해 토큰을 복호화,
           토큰을 만들 때 클레임으로 넣어둔 id값이 given절에서 만든 '
           유저 id 와 동일한지 확인

     */
    @DisplayName("validToken(): 만료된 토큰일 때 유효성 검증에 실패하기")
    @Test
    void validToken_invalidToken() {
        //given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
        .build().createToken(jwtProperties);
        //when
        boolean result= tokenProvider.validToken(token);
        //then
        assertThat(result).isFalse();
    }

    /*
    validToken_invalidToken()
    given : jjwt 라이브러리를 사용해 토큰을 생성
            만료기간은 1970년 1월 1일부터 현재 시간을 밀리초 단위로 치환한 값에 1000을 빼서
            이미,만료된 토큰으로 생성함
    when : 토큰 제공자의 vaildToken() 메서드를 호출해 유효한 토큰인지 검증한 뒤 결괏값을 반환받음
    then : 반환값이 false 인 것을 확인함
     */

    @DisplayName("validToken(): 유효한 토큰인 때에 유효성 검증에 성공")
    @Test
    void validToken_validToken(){
        //given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
        //when
        boolean result = tokenProvider.validToken(token);
        //then
        assertThat(result).isTrue();
    }
    /*
    validToken_validToken()
    given : jjwt 라이브러리를 사용해 토큰을 생성함.
            만료 시간은 현재 시간으로부터 14일 뒤로, 만료되지 않은 토큰으로 생성함
    when : 토큰 게공자의 validToken() 메서드를 호출해 유효한 토큰인지 검증한 뒤 결과값 반환
    then : 반환값이 true 인 것을 확인
     */

    //getAuthentication() 검증 테스트
    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있음")
    @Test
    void getAuthentication(){
        //given
        String userEmail = "user@Gamil.com";
        String token = JwtFactory.builder().subject(userEmail)
                .build().createToken(jwtProperties);

        //when
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername())
                .isEqualTo(userEmail);
    }

    /*
     getAuthentication()
     given : 토큰의 제목인 subject는 "user@email.com"라는 값을 사용
     when : 토큰 제공자의  getAuthentication()메서드를 호출해 인증 객체를 반환받음
     then : 반환받은 인증 객체의 유저 이름을 가져와 given절에서 설정한 subject값인
            "user@email.com"과 값이 같은지 확인함
     */

    //getUserId() 검증 테스트
    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있음")
    @Test
    void getUserId(){
        //given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id",userId))
                .build()
                .createToken(jwtProperties);

        //when
        Long userIdByToken = tokenProvider.getUserId(token);

        //then
        assertThat(userIdByToken).isEqualTo(userId);
    }

    /*
     getUserId()
     given : 이때 클레임을 추가, 키는 "id" 값은 1이라는 유저 ID
     when : 토큰 제공자의  getUserId() 메서드를 호출해 유저 ID를 반환받음
     then : 반환받은 유저 ID가 given 절에서 설정한 유저 ID값인 1과 같은지 확인
     */

}
