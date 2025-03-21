package me.dev.demo.service;

import lombok.RequiredArgsConstructor;
import me.dev.demo.domain.User;
import me.dev.demo.dto.AddUserRequest;
import me.dev.demo.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    // private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build()).getId();
    }
     //.password(bCryptPasswordEncoder.encode(dto.getPassword()))

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    
}

