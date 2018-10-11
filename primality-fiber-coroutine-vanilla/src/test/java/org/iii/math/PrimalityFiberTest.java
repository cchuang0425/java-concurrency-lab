package org.iii.math;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.iii.PrimalityFiberConfig;
import org.iii.mq.ResultReceiver;

import static org.iii.PrimalityFiberApplication.N;

public class PrimalityFiberTest {

    @Before
    public void setUp() {
        PrimalityFiberConfig.run();
    }

    @Test
    public void testRunFiber() {
        Thread resultReceiver = new Thread(ResultReceiver.getInstance());
        ExecutorService pool = PrimalityFiberConfig.getThreadPool();
        pool.execute(resultReceiver);
        pool.shutdown();

        PrimalityFiber fiber = new PrimalityFiber(N);
        FiberScheduler scheduler = PrimalityFiberConfig.getFiberPool();
        Fiber<List<Long>> primalityFiber = scheduler.newFiber(fiber);
        scheduler.shutdown();

        try {
            List<Long> primes = primalityFiber.start().get();

            System.out.println(primes);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(System.err);
        }
    }

    @After
    public void tearDown() {
        PrimalityFiberConfig.exit();
    }
}
