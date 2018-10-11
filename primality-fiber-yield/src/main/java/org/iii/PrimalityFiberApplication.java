package org.iii;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PrimalityFiberApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrimalityFiberApplication.class, args);
    }

    public static final long N = 45;

    @Bean
    @Autowired
    public CommandLineRunner runCLI(ApplicationContext context) {
        return args -> {

        };
    }
}
