package me.dev.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBothTokenRequest {
    private String email;
    private String accessToken;
    private String refreshToken;
}
