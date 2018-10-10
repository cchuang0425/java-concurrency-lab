package org.iii.math;

import java.util.ArrayList;
import java.util.List;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.channels.LongChannel;
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
        LongChannel fibGen = MathFibers.runFibGenerator();

        for (long i = 1L; i <= n; i++) {
            long fib = fibGen.receive();
            boolean prime = service.isPrime(fib);
            if (prime) {result.add(i);}
        }

        return result;
    }
}
