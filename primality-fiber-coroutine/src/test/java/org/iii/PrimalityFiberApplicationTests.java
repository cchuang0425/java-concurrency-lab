package org.iii;

import java.util.List;
import java.util.concurrent.ExecutionException;

import co.paralleluniverse.fibers.FiberScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import org.iii.math.PrimalityFiber;

import static org.iii.PrimalityFiberApplication.N;
import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimalityFiberApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAusRunCLI() throws ExecutionException, InterruptedException {
        PrimalityFiber fiber = context.getBean(PrimalityFiber.class, N);
        FiberScheduler exec = context.getBean(WORKER_POOL_NAME, FiberScheduler.class);
        List<Long> primes = exec.newFiber(fiber)
                                .start()
                                .get();
        System.out.println(primes);
    }

}
