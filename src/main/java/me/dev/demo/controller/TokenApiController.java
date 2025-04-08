package me.dev.demo.controller;

import lombok.RequiredArgsConstructor;
import me.dev.demo.dto.CreateAccessTokenRequest;
import me.dev.demo.dto.CreateAccessTokenResponse;
import me.dev.demo.service.MakeTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final MakeTokenService tokenService;
    //"새로운 액세스 토큰을 발급한다"
    @GetMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
