package org.iii.math;

import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.LongChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MathFibersTest {

    @Autowired
    @Qualifier(WORKER_POOL_NAME)
    private FiberScheduler pool;

    @Test
    @Suspendable
    public void testRunFibGen() throws InterruptedException, SuspendExecution {
        LongChannel channel = MathFibers.runFibGenerator();

        for (long i = 1L; i <= 45L; i++) {
            long fib = channel.receive();
            System.out.printf("fib(%d) is %d.%n", i, fib);
        }
    }
}
