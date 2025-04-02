package me.dev.demo.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.core.user.OAuth2User;

import me.dev.demo.dto.UserDTO;

import java.util.Map;
import java.util.Collection;

import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;


public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    public CustomOAuth2User (UserDTO userDTO){
        this.userDTO = userDTO;
    }



    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getUserName() {
        return userDTO.getUsername();
    }
}