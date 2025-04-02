package me.dev.demo.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Getter
@Setter
@Table(name = "tbl_member")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "role")
    private String role;

    @Column(name = "name")
    private String name;

    @Column(name = "user_name")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image")
    private String profileImage;
}