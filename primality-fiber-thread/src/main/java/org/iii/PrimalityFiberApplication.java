package org.iii;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJms
@EnableAsync
public class PrimalityFiberApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrimalityFiberApplication.class, args);
    }

    @Bean
    public CommandLineRunner runCLI() {
        return args -> {

        };
    }
}
