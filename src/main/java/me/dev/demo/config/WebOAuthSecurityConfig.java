package me.dev.demo.config;

import lombok.RequiredArgsConstructor;
import me.dev.demo.config.jwt.TokenProvider;
import me.dev.demo.config.oauth.CustomSuccessHandler;
import me.dev.demo.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.dev.demo.config.oauth.OAuth2SuccessHandler;
import me.dev.demo.repository.RefreshTokenRepository;
import me.dev.demo.service.OAuth2UserCustomService;
import me.dev.demo.service.CustomOAuth2UserService;
import me.dev.demo.service.UserService;
import me.dev.demo.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    private CustomOAuth2UserService customOAuth2UserService;
    private CustomSuccessHandler customSuccessHandler;
    private JWTUtil jwtUtil;
    

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
        .requestMatchers("/img/**", "/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        http.authorizeRequests()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        // http.oauth2Login()
        //         .loginPage("/login")
        //         .authorizationEndpoint()
        //         .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
        //         .and()
        //         .successHandler(oAuth2SuccessHandler())
        //         .userInfoEndpoint()
        //         .userService(oAuth2UserCustomService);

        // http.oauth2Login(oauth2 -> oauth2
        // .successHandler(customSuccessHandler)
        // .userInfoEndpoint(userInfoEndpointConfig->userInfoEndpointConfig
        // .userService(customOAuth2UserService)));

        http.oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(customSuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        http.logout()
                .logoutSuccessUrl("/login");


        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"));


        return http.build();
    }


    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,refreshTokenRepository,
        oAuth2AuthorizationRequestBasedOnCookieRepository(),
        userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
