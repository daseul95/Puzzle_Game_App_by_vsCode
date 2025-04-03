package me.dev.demo.config.oauth;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import me.dev.demo.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import me.dev.demo.service.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import org.springframework.http.HttpStatus;
import java.util.Collection;
import java.util.Iterator;
import org.springframework.security.core.GrantedAuthority;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JWTUtil jwtUtil;
    // private final RedisService redisService;
    
    @Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    	CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();
        
        // 토큰 생성시에 사용자명과 권한이 필요하니 준비하자
        String username = customUserDetail.getUserName();
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        

        String accessToken = jwtUtil.createJwt("access", username, role, 60000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);
        
    
        // redisService.setValues(username, refreshToken, Duration.ofMills(86400000L));
        
        // 응답
        response.setHeader("access", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        /*response.sendRedirect("http://localhost:8080/");*/        // 로그인 성공시 프론트에 알려줄 redirect 경로
    }
    
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);     // 쿠키가 살아있을 시간
        /*cookie.setSecure();*/         // https에서만 동작할것인지 (로컬은 http 환경이라 안먹음)
        /*cookie.setPath("/");*/        // 쿠키가 전역에서 동작
        cookie.setHttpOnly(true);       // http에서만 쿠키가 동작할 수 있도록 (js와 같은곳에서 가져갈 수 없도록)

        return cookie;
    }
    
}
