// // package me.dev.demo.service;

// // import me.dev.demo.domaUserWithoutAuth;
// import me.dev.demo.repository.UserRepository;
// import java.util.List;
// import java.util.Optional;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;


// @Service
// public class UserCrudService {
    
//     private final UserRepository userRepository;

//     @Autowired
//     public UserCrudService(UserRepository userRepository) {
//         this.userRepository = userRepository;
//     }

//     // Create (저장)
//     public UserWithoutAuth createUser(String email, String password) {
//         UserWithoutAuth user = UserWithoutAuth.builder()
//                         .email(email)
//                         .password(password)
//                         .build();

//        // 생성된 User 객체를 저장
//        return userRepository.save(user);
//     }

//     // Read (조회)
//     public List<UserWithoutAuth> getAllUsers() {
//         return userRepository.findAll();
//     }
//     public Optional<UserWithoutAuth> getUserByEmail(String email) {
//         return userRepository.findByEmail(email);
//     }
//     public UserWithoutAuth getUserById(Long id) {
//         return userRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//     }

//     // Update (수정)
//     public UserWithoutAuth updateUser(Long id, String email, String password) {
//         UserWithoutAuth user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//         user = new UserWithoutAuthserWithoutAuth(email, password); // 새롭게 업데이트한 사용자 정보로 교체
// //         return userRepository.save(user);
// //     }

// //     // Delete (삭제)
// //     public void deleteUser(Long id) {
// //         userRepository.deleteById(id);
// //     }

// // }/ }/ }/ }/ }/ }/ }/ }/ }/ }/ }/ }/ }/ }/ }/ }