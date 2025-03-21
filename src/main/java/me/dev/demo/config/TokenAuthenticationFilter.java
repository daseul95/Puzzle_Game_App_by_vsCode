package me.dev.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.dev.demo.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/* 이 필터는 액세스 토큰 값이 담긴 Authorization 헤더값을 가져온 뒤
* 액세스 토큰이 유효하다면 인증 정보를 설정함 
* */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "authorization";
    private final static String TOKEN_PREFIX = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        //가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);

        //가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보를 설정
        if(tokenProvider.validToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            //시큐리티 컨텍스트에 인증 정보를 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //컨텍스트 홀더에서 인증 정보를 가져오면 유저 객체가 반환됨. 
            //유저 객체에는 유저이름(username)과 권한 목록(authorities)와 같은 인증 정보가 포함됨
        }

        filterChain.doFilter(request,response);
    }

    private String getAccessToken(String authorizationHeader){
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)){
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        } 
        return null;  // 만약 값이 null이거나 Bearer 로 시작하지 않으면 null을 반환합니다
    }
}
