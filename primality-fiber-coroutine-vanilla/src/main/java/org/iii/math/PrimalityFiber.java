package org.iii.math;

import java.util.ArrayList;
import java.util.List;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.channels.Channel;

public class PrimalityFiber implements SuspendableCallable<List<Long>> {

    private long n;

    public PrimalityFiber(long n) {
        this.n = n;
    }

    @Override
    public List<Long> run() throws SuspendExecution, InterruptedException {
        List<Long> result = new ArrayList<>();

        for (long i = 1; i <= n; i++) {
            Long fibResult = calFib(i);
            Boolean primeResult = calPrime(fibResult);

            if (primeResult) {result.add(i);}
        }

        return result;
    }

    private Boolean calPrime(Long n) throws InterruptedException, SuspendExecution {
        try (Channel<Boolean> channel = MathService.getInstance()
                                                   .calPrime(n)) {
            return channel.receive();
        }
    }

    private Long calFib(long n) throws InterruptedException, SuspendExecution {
        try (Channel<Long> channel = MathService.getInstance()
                                                .calFib(n)) {
            return channel.receive();
        }
    }
}
