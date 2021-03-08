package com.xll.limit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitApplication.class, args);
    }

}
