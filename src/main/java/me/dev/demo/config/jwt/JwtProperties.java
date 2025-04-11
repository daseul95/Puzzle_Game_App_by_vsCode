package me.dev.demo.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix="jwt")
public class JwtProperties{
    private String issuer;
    private String secretKey;
    private String secret;
}
