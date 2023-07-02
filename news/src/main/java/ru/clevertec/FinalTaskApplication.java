package ru.clevertec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FinalTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalTaskApplication.class, args);
    }
}
