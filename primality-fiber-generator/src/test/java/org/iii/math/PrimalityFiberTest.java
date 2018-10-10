package org.iii.math;

import java.util.List;
import java.util.concurrent.ExecutionException;

import co.paralleluniverse.fibers.FiberScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimalityFiberTest {

    @Autowired
    @Qualifier(WORKER_POOL_NAME)
    private FiberScheduler pool;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testPrimalityFiber() throws ExecutionException, InterruptedException {
        PrimalityFiber primFib = context.getBean(PrimalityFiber.class, 30L);

        List<Long> primes = pool.newFiber(primFib)
                                .start()
                                .get();

        System.out.println(primes);
    }
}
