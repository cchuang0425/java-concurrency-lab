package org.iii.math;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import org.iii.PrimalityFiberApplication;

@Component
public class PrimalityFibonacciCache {

    private List<Long> primeResults;
    private LinkedBlockingQueue<Boolean> signal;

    public PrimalityFibonacciCache() {
        this.primeResults = Collections.synchronizedList(new LinkedList<>());
        this.signal = new LinkedBlockingQueue<>(1);
    }

    public void putPrimeFibs(long i, boolean prime) {
        if (prime) {this.primeResults.add(i);}
        if (i == PrimalityFiberApplication.N) {signal.offer(true);}
    }

    public List<Long> getPrimeResults() {
        return primeResults;
    }

    public boolean waitForSignal() throws InterruptedException {
        return this.signal.take();
    }
}
