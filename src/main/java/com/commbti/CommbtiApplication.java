package com.commbti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommbtiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommbtiApplication.class, args);
    }

}
