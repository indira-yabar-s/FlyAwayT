package com.example.flyawayt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FlyAwayTApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlyAwayTApplication.class, args);
    }
}
