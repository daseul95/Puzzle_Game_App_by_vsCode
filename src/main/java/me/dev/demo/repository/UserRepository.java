package me.dev.demo.repository;

import me.dev.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.cert.PKIXRevocationChecker;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 email로 찾기
    Optional<User> findByEmail(String email);
    User findByUserName(String userName);
}