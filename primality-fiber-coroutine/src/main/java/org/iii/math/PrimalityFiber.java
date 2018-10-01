package org.iii.math;

import java.util.ArrayList;
import java.util.List;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.common.BaseFiber;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class PrimalityFiber extends BaseFiber implements SuspendableCallable<List<Long>> {

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
            Long fibResult = receiveFromChannel(service.calFib(i));
            Boolean primeResult = receiveFromChannel(service.calPrime(fibResult));
            if (primeResult) {result.add(i);}
        }

        return result;
    }
}
