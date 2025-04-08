package me.dev.demo.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private Long id;

    @Column(name="title",nullable = false)
    private String title;

    @Column(name="content",nullable = false)
    private String content;

    @Column(name="author",nullable = false)
    private String author;

    @CreatedDate
    @Column(name="createdAt")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name="updatedAt")
    private LocalDateTime updateAt;

    @Builder
    public Article(String author,String title,String content){
        this.author = author;
        this.title=title;
        this.content = content;
    }

    public void update(String title,String content){
        this.title = title;
        this.content = content;
    }
}