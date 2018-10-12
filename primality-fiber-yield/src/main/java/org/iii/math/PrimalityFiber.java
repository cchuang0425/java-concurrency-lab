package org.iii.math;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.LongChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.PrimalityFiberApplication;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class PrimalityFiber implements SuspendableRunnable {

    @Autowired
    private PrimalityFibonacciCache cache;

    @Autowired
    private MathService service;

    private Channel<Long> counter;

    public PrimalityFiber(Channel<Long> counter) {
        this.counter = counter;
    }

    @Override
    public void run() throws SuspendExecution, InterruptedException {
        long n = 0L;

        while (n <= PrimalityFiberApplication.N) {
            n = counter.receive();
            long fib = service.calFib(n);
            boolean prime = service.isPrime(fib);

            System.out.printf("fib(%d)'s result is %s%n", n, prime);

            cache.putPrimeFibs(n, prime);

            Fiber.yield();
        }
    }
}
