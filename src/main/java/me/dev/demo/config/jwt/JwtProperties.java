package me.dev.demo.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("jwt")
public class JwtProperties{
    private String issuer= "osllsl123o@gmail.com";
    private String secretKey = "puzzle_game";
}
