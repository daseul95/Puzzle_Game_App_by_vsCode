package me.dev.demo.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public void testEndpoint() {
        System.out.println(io.jsonwebtoken.Jwts.class.getPackage().getImplementationVersion());


    }
}