//package me.dev.demo.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import me.dev.demo.domain.Article;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//public class AddArticleRequest {
//
//    private String title;
//    private String content;
//    private String author;
//
//    public Article toEntity(String author){
//        return Article.builder().title(title).content(content).author(author).build();
//    }
//}