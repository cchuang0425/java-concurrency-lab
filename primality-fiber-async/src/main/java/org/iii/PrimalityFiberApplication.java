package org.iii;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.publisher.Flux;

import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;

@SpringBootApplication
@EnableJms
@EnableAsync
public class PrimalityFiberApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimalityFiberApplication.class, args);
	}

    public static final long N = 45;

    @Bean
    @Autowired
    public CommandLineRunner runCLI(ApplicationContext context) {
        return args -> {
            if (args == null || args.length == 0) {
                return;
            } else {
                // Flux<Integer> primes = Flux.range(1, (int) N)

                System.exit(0);
            }
        };
    }
}
