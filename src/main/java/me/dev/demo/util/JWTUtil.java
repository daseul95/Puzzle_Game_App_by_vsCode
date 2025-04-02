package me.dev.demo.util;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import javax.crypto.KeyGenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
    private SecretKey secretKey;
    
    public JWTUtil(@Value("${jwt.secretKey}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }

    public SecretKey getSecretKey(){
        return secretKey;
    }
    

    public String getUsername(String token) {
        SecretKey secretKey = getSecretKey();
        JwtParser parser = Jwts.parser().setSigningKey(secretKey);
        return parser.parseClaimsJwt(token).getBody().get("username",String.class);
    }    
    // 권한 추출
    public String getRole(String token) {
        SecretKey secretKey = getSecretKey();
        JwtParser parser = Jwts.parser().setSigningKey(secretKey);
        return parser.parseClaimsJwt(token).getBody().get("role",String.class);
    }

    // token 유효확인
    public Boolean isExpired(String token) {
        SecretKey secretKey = getSecretKey();
        JwtParser parser = Jwts.parser().setSigningKey(secretKey);
        return parser.parseClaimsJwt(token).getBody().getExpiration().before(new Date());
    }
    
    // accessToken인지 refreshToken인지 확인
    public String getCategory(String token) {
        SecretKey secretKey = getSecretKey();
    	// return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
        JwtParser parser = Jwts.parser().setSigningKey(secretKey);
        return parser.parseClaimsJwt(token).getBody().get("category",String.class);

    }
    
    // JWT 발급
    public String createJwt(String category, String username, String role, Long expiredMs) {
    	return Jwts.builder()
        	.claim("category", category)
            .claim("username", username)
            .claim("role", role)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }
}