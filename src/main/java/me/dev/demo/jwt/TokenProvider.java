package me.dev.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.dev.demo.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()),user);
    }

    // JWT 토큰 생성 메서드
    // set 계열의 메서드를 통해 여러 값을 지정
    private String makeToken(Date expiry,User user){ // 인자로는 만료시간,유저 정보 받아옴
        Date now = new Date();

        return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // 헤더 typ: JWT
                // 내용 iss : oslsl123@gamil.com(properties 파일에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail()) // 내용 usb : 유저의 이메일
                .claim("id",user.getId()) // 클레임 id : 유저의 ID
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact(); //토큰을 만들 때는 프로퍼티즈 파일에 설정해둔 비밀값과 함께 HS256 방식으로 암호화
                                
    }


    // JWT 토큰 유효성 검증 메서드
    // 프로퍼티즈 파일에 선언한 비밀값과 함께 토큰 복호화를 진행
    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true; 
        }catch (Exception e){  
            return false; //복호화 과정에서 에러가 나면 유효하지 않은 토큰
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    // Authentication 객체를 반환하는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        //토큰을 복호화 한 뒤 클레임을 가져오는 private 메서드인 getClaim()
        //클레임 정보를 받아옴
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new
                SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities), token, authorities);
    }

    //토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id",Long.class);
    }

    private Claims getClaims(String token){
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

}


