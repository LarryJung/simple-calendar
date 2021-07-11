package com.larry.lallender.lallender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class LallenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LallenderApplication.class, args);
    }

}
