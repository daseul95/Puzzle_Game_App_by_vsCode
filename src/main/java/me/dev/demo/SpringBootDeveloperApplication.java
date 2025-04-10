package me.dev.demo;

import me.dev.demo.config.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"me.dev.demo.service","me.dev.demo.config","me.dev.demo.controller"})
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SpringBootDeveloperApplication {
    public static void main(String[] args){
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}