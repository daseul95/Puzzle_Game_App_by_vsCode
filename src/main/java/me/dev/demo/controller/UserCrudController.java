package me.dev.demo.controller;
import me.dev.demo.dto.UserRequestDto;
import me.dev.demo.domain.User;
import me.dev.demo.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import me.dev.demo.service.UserCrudService;

@RestController
@RequestMapping("/users") // 모든 엔드포인트의 기본 경로 설정
@RequiredArgsConstructor // 생성자 주입을 자동으로 처리
public class UserCrudController {
    private final UserCrudService userService;

    // 1️ 유저 생성 (POST)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequestDto requestDto) {
        User user = userService.createUser(requestDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.ok(user);
    }

    // 2️ 유저 조회 (GET)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // 3️ 유저 전체 조회 (GET)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 4️ 유저 정보 수정 (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        User updatedUser = userService.updateUser(id, requestDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.ok(updatedUser);
    }

    // 5️ 유저 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}