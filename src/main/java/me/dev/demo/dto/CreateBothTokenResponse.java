package me.dev.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateBothTokenResponse {
    private String accessToken;
    private String refreshToken;
}
