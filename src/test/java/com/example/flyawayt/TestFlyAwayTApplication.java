package com.example.flyawayt;

import org.springframework.boot.SpringApplication;

public class TestFlyAwayTApplication {

    public static void main(String[] args) {
        SpringApplication.from(FlyAwayTApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
