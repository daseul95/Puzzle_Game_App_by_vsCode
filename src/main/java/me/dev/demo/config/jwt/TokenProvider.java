package me.dev.demo.config.jwt;
import lombok.Builder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import me.dev.demo.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import lombok.Builder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.crypto.SecretKey;
import java.util.Collections;
import static java.util.Collections.emptyMap;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private String subject = "test@email.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime()+Duration.ofDays(14).toMillis());
    private Map<String,Object> claims = emptyMap();
    

    @Builder
    public TokenProvider(JwtProperties jwt,String subject,Date issuedAt,Date expiration,Map<String,Object> claims){
        this.jwtProperties = jwt;
        this.subject = subject !=null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static TokenProvider withDefaultValues(){
        return TokenProvider.builder().build();
    }

    public String createToken(JwtProperties jwtProperties){
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
        .setSubject(subject)
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer(jwtProperties.getIssuer())
        .setIssuedAt(issuedAt)
        .setExpiration(expiration)
        .addClaims(claims)
        .signWith(key,SignatureAlgorithm.HS256)
        .compact();
    }
    
    
    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();  // 현재 시간을 나타내는 Date 객체를 생성합니다.
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()),user);  
    } // 지금 시간을 기준으로 지정된 만료시간(Duration)만큼 
      // 더한 시점을 만료시간을 설정해서 토큰을 생성함

    private String makeToken(Date expiry,User user){
        Date now = new Date();
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

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
    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true; 
        }catch (Exception e){
            return false;
        }
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
    
    private Claims getClaims(String token){
        return Jwts.parser() 
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

}