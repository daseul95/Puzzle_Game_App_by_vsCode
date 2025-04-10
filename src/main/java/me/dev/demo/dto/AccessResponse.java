package me.dev.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class AccessResponse {
    private String accessToken;
    private Boolean isAvails;
}
