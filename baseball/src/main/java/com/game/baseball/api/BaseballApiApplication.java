package com.game.baseball.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BaseballApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseballApiApplication.class, args);
    }
}
