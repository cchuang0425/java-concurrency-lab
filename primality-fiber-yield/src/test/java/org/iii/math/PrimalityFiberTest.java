package org.iii.math;

import java.util.List;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.LongChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimalityFiberTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PrimalityFibonacciCache cache;

    @Autowired
    private FiberScheduler scheduler;

    @Test
    @Suspendable
    public void testSingleFiber() throws InterruptedException {
        LongChannel counter = MathFibers.runCounterGenerator();
        PrimalityFiber runner = context.getBean(PrimalityFiber.class, counter);
        Fiber<Void> fiber = new Fiber<>(scheduler, runner);

        long startTime = System.currentTimeMillis();

        fiber.start();

        if (cache.waitForSignal()) {
            List<Long> results = cache.getPrimeResults();
            System.out.println(results);
            System.out.printf("time used %d%n", System.currentTimeMillis() - startTime);
        }
    }

    @Test
    @Suspendable
    public void testDoubleFibers() throws InterruptedException {
        LongChannel counter = MathFibers.runCounterGenerator();
        PrimalityFiber runner1 = context.getBean(PrimalityFiber.class, counter);
        Fiber<Void> fiber1 = new Fiber<>(scheduler, runner1);
        PrimalityFiber runner2 = context.getBean(PrimalityFiber.class, counter);
        Fiber<Void> fiber2 = new Fiber<>(scheduler, runner2);

        long startTime = System.currentTimeMillis();

        fiber1.start();
        fiber2.start();

        if (cache.waitForSignal()) {
            List<Long> results = cache.getPrimeResults();
            System.out.println(results);
            System.out.printf("time used %d%n", System.currentTimeMillis() - startTime);
        }
    }

    @Test
    @Suspendable
    public void testTribleFibers() throws InterruptedException {
        LongChannel counter = MathFibers.runCounterGenerator();
        PrimalityFiber runner1 = context.getBean(PrimalityFiber.class, counter);
        Fiber<Void> fiber1 = new Fiber<>(scheduler, runner1);
        PrimalityFiber runner2 = context.getBean(PrimalityFiber.class, counter);
        Fiber<Void> fiber2 = new Fiber<>(scheduler, runner2);
        PrimalityFiber runner3 = context.getBean(PrimalityFiber.class, counter);
        Fiber<Void> fiber3 = new Fiber<>(scheduler, runner3);

        long startTime = System.currentTimeMillis();

        fiber1.start();
        fiber2.start();
        fiber3.start();

        if (cache.waitForSignal()) {
            List<Long> results = cache.getPrimeResults();
            System.out.println(results);
            System.out.printf("time used %d%n", System.currentTimeMillis() - startTime);
        }
    }
}