//이 UserService는 auth 확인을 합니다. 그러므로 아직 curd 작업중이므로 꺼놉니다.

// package me.dev.demo.service;

// import lombok.RequiredArgsConstructor;
// import me.dev.demo.domain.User;
// import me.dev.demo.dto.AddUserRequest;
// import me.dev.demo.repository.UserRepository;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;

// @RequiredArgsConstructor
// @Service
// public class UserService {

//     private final UserRepository userRepository;
    
//     // private final BCryptPasswordEncoder bCryptPasswordEncoder;

//     public Long save(AddUserRequest dto) {
//         return userRepository.save(User.builder()
//                 .email(dto.getEmail())
//                 .password(dto.getPassword())
//                 .build()).getId();
//     }
//      //.password(bCryptPasswordEncoder.encode(dto.getPassword()))

//     public User findById(Long userId) {
//         return userRepository.findById(userId)
//                 .orElseThrow(() -> new IllegalArgumentException("Unexpected user")
//                 );
//     }
// }