package me.dev.demo.service;

import lombok.RequiredArgsConstructor;
import me.dev.demo.domain.User;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import me.dev.demo.repository.UserRepository;



@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
