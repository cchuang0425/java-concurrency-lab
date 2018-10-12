package org.iii.math;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.iii.PrimalityFiberApplication.N;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MathFibersTest {

    @Test
    @Suspendable
    public void testCounter() throws InterruptedException, SuspendExecution {
        Channel<Long> channel = MathFibers.runCounterGenerator();

        long counter = 0L;
        for (long i = 1L; i <= 45L; i++) {
            counter = channel.receive();
            System.out.printf("%dth counter is %d.%n", i, counter);
        }

        Assert.assertEquals(N, counter);
    }
}
