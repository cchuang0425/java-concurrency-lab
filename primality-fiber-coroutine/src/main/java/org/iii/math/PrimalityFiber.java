package org.iii.math;

import java.util.ArrayList;
import java.util.List;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.channels.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class PrimalityFiber implements SuspendableCallable<List<Long>> {

    @Autowired
    private MathService service;

    private long n;

    public PrimalityFiber(long n) {
        this.n = n;
    }

    @Override
    public List<Long> run() throws SuspendExecution, InterruptedException {
        List<Long> result = new ArrayList<>();

        for (long i = 1; i <= n; i++) {
            Long fibResult = calFib(n);
            Boolean primeResult = calPrime(fibResult);

            if (primeResult) {result.add(i);}
        }

        return result;
    }

    private Boolean calPrime(Long n) throws InterruptedException, SuspendExecution {
        try (Channel<Boolean> channel = service.calPrime(n)) {
            return channel.receive();
        }
    }

    private Long calFib(long n) throws InterruptedException, SuspendExecution {
        try (Channel<Long> channel = service.calFib(n)) {
            return channel.receive();
        }
    }
}
