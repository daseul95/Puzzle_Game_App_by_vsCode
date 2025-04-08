package me.dev.demo.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import lombok.Builder;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;

    @Column(name="user_id",nullable=false,unique=true)
    private Long userId;

    @Column(name="refresh_token",nullable = false)
    private String refreshToken;

    @CreationTimestamp
    @Column(name="iat_day", nullable=false)
    private LocalDateTime issuedAt;

    @Column(name="expire_day", nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public RefreshToken(Long userId,String refreshToken){
        this.userId=userId;
        this.refreshToken=refreshToken;
    }

    public RefreshToken update(String newRefreshToken){
        this.refreshToken = newRefreshToken;
        return this;
    }

    @PrePersist
    public void prePersist() {
    if (this.expiresAt == null) {
        this.expiresAt = LocalDateTime.now().plusDays(7);
    }
}
}
