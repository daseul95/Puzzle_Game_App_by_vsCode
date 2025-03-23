package me.dev.demo.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
public class AddUserRequest {
    private String email;
    private String password;
}