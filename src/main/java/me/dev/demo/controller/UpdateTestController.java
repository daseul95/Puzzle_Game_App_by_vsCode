package me.dev.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import me.dev.demo.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UpdateTestController {

    @Autowired
    private UserService userService;

    // 패스워드 변경 요청 처리
    @PutMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id, @RequestParam String newPassword) {
        try {
            userService.updatePassword(id, newPassword);
            return "Password updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}