package org.iii;

import java.util.List;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.iii.math.PrimalityFiber;

import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;

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
            if (args == null || args.length == 0) {
                return;
            } else {
                System.out.println("run PrimalityFiber...");

                PrimalityFiber primalityFiber = context.getBean(PrimalityFiber.class, N);
                FiberScheduler scheduler = context.getBean(WORKER_POOL_NAME, FiberScheduler.class);
                Fiber<List<Long>> fiber = scheduler.newFiber(primalityFiber);
                scheduler.shutdown();

                List<Long> primes = fiber.start().get();

                System.out.println(primes);

                System.exit(0);
            }
        };
    }
}
