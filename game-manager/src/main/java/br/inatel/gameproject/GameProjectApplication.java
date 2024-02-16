package br.inatel.gameproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GameProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameProjectApplication.class, args);
    }
}