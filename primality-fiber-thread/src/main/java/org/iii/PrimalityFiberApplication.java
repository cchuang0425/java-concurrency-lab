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

import org.iii.math.PrimalityFiber;

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
                PrimalityFiber fiber = context.getBean(PrimalityFiber.class, N);
                ThreadPoolTaskExecutor exec = context.getBean(WORKER_POOL_NAME, ThreadPoolTaskExecutor.class);

                List<Long> primes = exec.submit(fiber)
                                        .get();

                System.out.println(primes);

                System.exit(0);
            }
        };
    }
}
