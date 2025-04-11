package me.dev.demo.config.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.Builder;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import me.dev.demo.domain.User;
import me.dev.demo.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import lombok.Builder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private static String HEADER_AUTHORIZATION = "Authorization";
    private static String TOKEN_PREFIX = "Bearer ";

    public String generateToken(User user, Duration expiredAt) throws Exception{
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()),user);  
    }

    private String makeToken(Date expiry,User user) throws Exception{
        Date now = new Date();


        String secretKey = jwtProperties.getSecretKey();
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        System.out.println("생성된 키 : "+  key);

        String token = Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail()) 
                .claim("id",user.getId())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

                return token;
            }
    public boolean validToken(String token) throws Exception{

        System.out.println("Received token: " + token);

        String secretKey = jwtProperties.getSecretKey();
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        System.out.println("검증될때 쓰이는 키 : "+  key);

        try{
            Claims claims = Jwts.parserBuilder().setSigningKey(key)
                    .build().parseClaimsJws(token).getBody();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
    
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new
                SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities), token, authorities);
    }
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id",Long.class);
    }
    public String getUserEmail(String token){
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        return email;
    }
    
    private Claims getClaims(String token){
        return Jwts.parser() 
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

}