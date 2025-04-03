package me.dev.demo.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name="users")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable=false)
    private Long id;

    @Column(name="email",nullable = false,unique=true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name="user_name",unique=true)
    private String userName;
    
    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "role")
    private String role; 

    @Builder
    public User(String email,String password,String name,String userName,String profileImage,String role){
        this.email= email;
        this.password = password;
        this.name=name;
        this.userName=userName;
        this.profileImage = profileImage;
        this.role= role;
    }

    public User update(String userName){
        this.userName=userName;

        return this;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return email;
    }
    public String getUserName(){
        return userName;
    }

    @Override
    public String getPassword(){
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}