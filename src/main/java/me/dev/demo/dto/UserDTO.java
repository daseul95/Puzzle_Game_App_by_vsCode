package me.dev.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private String role;
    private String name;
    private String username;
    private String email;
    private String profileImage;
}