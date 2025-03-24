// package me.dev.demo.service;


// import lombok.RequiredArgsConstructor;
// import me.dev.demo.domain.UserWithAuth;
// import me.dev.demo.repository.UserRepository;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.stereotype.Service;

// @RequiredArgsConstructor
// @Service
// public class UserDetailService implements UserDetailsService {

//     private final UserRepository userRepository;

//     @Override
//     public UserWithAuth loadUserByUsername(String email) {
//         return userRepository.findByEmail(email)
//                 .orElseThrow(() -> new IllegalArgumentException((email)));
//     }
    
// }